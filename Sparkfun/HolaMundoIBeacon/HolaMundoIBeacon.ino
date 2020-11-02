// -*- mode: c++ -*-

// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-15
// ----------------------------------------------------------

#include <bluefruit.h>
#undef min // vaya tela, están definidos en bluefruit.h y  !
#undef max // colisionan con los de la biblioteca estándar

// --------------------------------------------------------------
// --------------------------------------------------------------
//#include "LED.h"
#include "Pantallita.h"
#include "Medidor.h"

// --------------------------------------------------------------
// --------------------------------------------------------------
namespace Globales {

//LED elLED ( /* NUMERO DEL PIN LED = */ 7 );

Pantallita pantallita;

Medidor elMedidor;
};

// --------------------------------------------------------------
// --------------------------------------------------------------
#include "EmisoraBLE.h"
#include "Publicador.h"

// --------------------------------------------------------------
// --------------------------------------------------------------
namespace Globales {

Publicador elPublicador;

double medicion;
uint16_t redondeado;

uint8_t cont = 0;

SemaphoreHandle_t xSemaphore_1;
SemaphoreHandle_t xSemaphore_2;
TimerHandle_t xAutoReloadTimer;
BaseType_t xTimer1Started;
}; // namespace

// --------------------------------------------------------------
// --------------------------------------------------------------

//**************************************************//
//          enviarSemaforoCallback()               //
// -----------------------------------------------//
// Cuando el temporizador llega a 0,             //
// activa la función tomarMedida()              //
//*********************************************//
void enviarSemaforoCallback(TimerHandle_t xTimer){
    using namespace Globales;
    xSemaphoreGive( xSemaphore_1);
}


//****************************************//
//          tomarMedidas()               //
// -------------------------------------//
// Toma medidas del sensor.            //
//***********************************//
void tomarMedidas(void *pvParameter) {
  using namespace Globales;
  for (;;) {
    // Si se toma el semáforo 1...
    if ( xSemaphoreTake( xSemaphore_1, portMAX_DELAY)) {
      // ...coge y aproxima las medidas...
      Serial1.print('\r'); // Inicia una lectura del sensor. Ahora hay que espera a que nos envíe algo de vuelta!
      pantallita.escribir("Lectura del sensor iniciada...esperando... \n");
      medicion = elMedidor.medirSO2();
      redondeado = floor(medicion);

      cont++;

      xSemaphoreGive( xSemaphore_2);
    }
  }
}

//****************************************//
//          publicarMedidas()            //
// -------------------------------------//
// Envía por Bluetooth las medidas     //
// tomadas                            //
//***********************************//
void publicarMedidas(void *pvParameter) {
  using namespace Globales;
  for (;;) {
    // Si se toma el semáforo 2...
    if ( xSemaphoreTake( xSemaphore_2, portMAX_DELAY)) {
      // ...las emite como una baliza.
      pantallita.escribir("Publicando datos \n");
      elPublicador.publicarSO2(cont,
                               cont,
                               1000 // intervalo de emisión
                              );
      imprimirMedidas();
    }
  }
}


//***************************************//
//          imprimirMedidas()           //
// ------------------------------------//
// Imprime en la terminan las medidas //
// obtenidas.                        //
//**********************************//
void imprimirMedidas() {
  using namespace Globales;
  
  //pantallita.escribir("Valor calibrado: \n");
  //Serial.println(medicion);


  //pantallita.escribir("Valor calibrado redondeado: \n");
  //Serial.println(redondeado);

  pantallita.escribir("Valor calibrado redondeado: \n");
  Serial.println(cont); 
}



// --------------------------------------------------------------
// --------------------------------------------------------------

void setup() {
  using namespace Globales;
  pantallita.iniciarPantallita( 9600 );
  pantallita.escribir("Setup empieza \n");

  elMedidor.iniciarMedidor( 9600 );
  elPublicador.encenderEmisora();

  // Crear los dos semáforos binarios
  xSemaphore_1 = xSemaphoreCreateBinary();
  xSemaphore_2 = xSemaphoreCreateBinary();

  // Temporizador para desencadenar interrupción
  //xTimerCreate(Nombre Temporizador, Tiempo en Ticks (20 ticks = 1s), pdTRUE(AutoReload)/pdFALSE(OneShot),Id Temporizador, Función de Interrupción) 
  xAutoReloadTimer = xTimerCreate("AutoReaload", 20, pdTRUE, 0, enviarSemaforoCallback);
  xTimer1Started = xTimerStart(xAutoReloadTimer, 0);
  
  // Crea una tarea
  //xTaskCreate(Puntero a Función, Nombre de Tarea, Número de Palabras, Valor de Entrada, Prioridad, Handler)
  xTaskCreate(&tomarMedidas, "tomarMedidas", 2048, NULL, 1, NULL);
  xTaskCreate(&publicarMedidas, "publicarMedidas", 2048, NULL, 1, NULL);

  pantallita.escribir("Setup termina \n");
}


// --------------------------------------------------------------
// --------------------------------------------------------------
void loop() {
}
