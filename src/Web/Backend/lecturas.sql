-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 05-11-2020 a las 23:40:26
-- Versión del servidor: 10.1.37-MariaDB
-- Versión de PHP: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `lecturas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `distanciaypasosrecorridos`
--

CREATE TABLE `distanciaypasosrecorridos` (
  `idUsuario` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `momento` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `pasos` text COLLATE utf8_spanish_ci NOT NULL,
  `distancia` text COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `distanciaypasosrecorridos`
--

INSERT INTO `distanciaypasosrecorridos` (`idUsuario`, `momento`, `pasos`, `distancia`) VALUES
('nachonachete@gmail.com', 'hoy', '1234', '200');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lecturas`
--

CREATE TABLE `lecturas` (
  `momento` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `ubicacion` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `valor` int(11) NOT NULL,
  `idMagnitud` varchar(120) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `lecturas`
--

INSERT INTO `lecturas` (`momento`, `ubicacion`, `valor`, `idMagnitud`) VALUES
('hoy', '78978678675', 1, 'SO2');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `logros`
--

CREATE TABLE `logros` (
  `idLogro` int(11) NOT NULL,
  `Descripcion` text COLLATE utf8_spanish_ci NOT NULL,
  `Dificultad` int(11) NOT NULL,
  `NombreLogro` text COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `logros`
--

INSERT INTO `logros` (`idLogro`, `Descripcion`, `Dificultad`, `NombreLogro`) VALUES
(1, 'Haz 2 pasos', 2, 'Imposible 100%');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `logrosconseguidosusuario`
--

CREATE TABLE `logrosconseguidosusuario` (
  `idUsuario` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `idLogro` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `logrosconseguidosusuario`
--

INSERT INTO `logrosconseguidosusuario` (`idUsuario`, `idLogro`) VALUES
('nachonachete@gmail.com', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `magnitudes`
--

CREATE TABLE `magnitudes` (
  `idMagnitud` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `Descripcion` varchar(500) COLLATE utf8_spanish_ci NOT NULL,
  `limiteMax` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `magnitudes`
--

INSERT INTO `magnitudes` (`idMagnitud`, `Descripcion`, `limiteMax`) VALUES
('SO2', 'mala', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `quehamedidoque`
--

CREATE TABLE `quehamedidoque` (
  `idUsuario` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `momento` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `ubicacion` varchar(120) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `idUsuario` varchar(120) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` text COLLATE utf8_spanish_ci NOT NULL,
  `apellidos` text COLLATE utf8_spanish_ci NOT NULL,
  `contrasenya` text COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`idUsuario`, `nombre`, `apellidos`, `contrasenya`) VALUES
('admin@admin.com', 'Michel', 'Soft', 'admin'),
('nachonachete@gmail.com', 'Nacho', 'Nachete', '123456789');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `distanciaypasosrecorridos`
--
ALTER TABLE `distanciaypasosrecorridos`
  ADD KEY `idUsuario` (`idUsuario`,`momento`),
  ADD KEY `momento` (`momento`);

--
-- Indices de la tabla `lecturas`
--
ALTER TABLE `lecturas`
  ADD PRIMARY KEY (`momento`,`ubicacion`),
  ADD KEY `idMagnitud` (`idMagnitud`),
  ADD KEY `ubicacion` (`ubicacion`);

--
-- Indices de la tabla `logros`
--
ALTER TABLE `logros`
  ADD PRIMARY KEY (`idLogro`);

--
-- Indices de la tabla `logrosconseguidosusuario`
--
ALTER TABLE `logrosconseguidosusuario`
  ADD KEY `idUsuario` (`idUsuario`,`idLogro`),
  ADD KEY `idLogro` (`idLogro`);

--
-- Indices de la tabla `magnitudes`
--
ALTER TABLE `magnitudes`
  ADD PRIMARY KEY (`idMagnitud`);

--
-- Indices de la tabla `quehamedidoque`
--
ALTER TABLE `quehamedidoque`
  ADD KEY `idUsuario` (`idUsuario`,`momento`,`ubicacion`),
  ADD KEY `ubicacion` (`ubicacion`),
  ADD KEY `momento` (`momento`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`idUsuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `logros`
--
ALTER TABLE `logros`
  MODIFY `idLogro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `distanciaypasosrecorridos`
--
ALTER TABLE `distanciaypasosrecorridos`
  ADD CONSTRAINT `distanciaypasosrecorridos_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`),
  ADD CONSTRAINT `distanciaypasosrecorridos_ibfk_2` FOREIGN KEY (`momento`) REFERENCES `lecturas` (`momento`);

--
-- Filtros para la tabla `lecturas`
--
ALTER TABLE `lecturas`
  ADD CONSTRAINT `lecturas_ibfk_1` FOREIGN KEY (`idMagnitud`) REFERENCES `magnitudes` (`idMagnitud`);

--
-- Filtros para la tabla `logrosconseguidosusuario`
--
ALTER TABLE `logrosconseguidosusuario`
  ADD CONSTRAINT `logrosconseguidosusuario_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`),
  ADD CONSTRAINT `logrosconseguidosusuario_ibfk_2` FOREIGN KEY (`idLogro`) REFERENCES `logros` (`idLogro`);

--
-- Filtros para la tabla `quehamedidoque`
--
ALTER TABLE `quehamedidoque`
  ADD CONSTRAINT `quehamedidoque_ibfk_1` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`),
  ADD CONSTRAINT `quehamedidoque_ibfk_2` FOREIGN KEY (`momento`) REFERENCES `lecturas` (`momento`),
  ADD CONSTRAINT `quehamedidoque_ibfk_3` FOREIGN KEY (`ubicacion`) REFERENCES `lecturas` (`ubicacion`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
