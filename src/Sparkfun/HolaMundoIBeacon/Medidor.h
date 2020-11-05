// -*- mode: c++ -*-

// ----------------------------------------------------------
// David Fernández Fuster
// 2020-10-15
// ----------------------------------------------------------

#ifndef MEDIDOR_H_INCLUIDO
#define MEDIDOR_H_INCLUIDO

// ------------------------------------------------------
// ------------------------------------------------------
class Medidor {

    // .....................................................
    // .....................................................
  private:

  public:

    // .....................................................
    // constructor
    // .....................................................
    Medidor() {
    } // ()

    // .....................................................
    // .....................................................
    void iniciarMedidor(long baudios) {
      // las cosas que no se puedan hacer en el constructor, if any

      Serial1.begin(baudios); // Puerto serie hardware para comunicación con el sensor, 8 bit, no parity, 1 stop bit, 3.3V

      Serial.println("medidor iniciado con baudios  ");

    } // ()

    // .....................................................
    // medirSO2() <-
    // -> Z
    // // Mide SO2 y lo devuelve calibrado para su envío.
    // .....................................................
    double medirSO2(void)
    {
      int sensorData [11];
      for (int i = 0; i < 11; i++) {
        while ( !Serial1 ) delay(10);
        /*while ( Serial1.available() == false) {
          delay(10);
          }*/
        sensorData[i] = Serial1.parseInt();
      } // for

      int valorPPB = sensorData[1];
      Serial.println("El valorPPB es: ");
      Serial.println(valorPPB);
      if (valorPPB == 0) {
        Serial.println("ESTA DESCONECTADO EL SERIAL");
        return 15000;
      }// if

      else {
        Serial.println("Valor sin calibrar: ");
        Serial.println(valorPPB);

        double pesoMolecularSO2 = 64.06;
        double res = ((valorPPB * 12.187 * pesoMolecularSO2) / (273.15 + sensorData[5])) / 1000;
        Serial.println("Valor calibrado: ");
        Serial.println(res);
        return res;
      }// else
    }//()



}; // class

// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
#endif
