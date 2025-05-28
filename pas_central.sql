-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-05-2025 a las 20:20:03
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `pas_central`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `formularios`
--

CREATE TABLE `formularios` (
  `fecha` datetime DEFAULT current_timestamp(),
  `examinador` text NOT NULL,
  `examinador_cedula` int(11) NOT NULL,
  `cedula` int(11) NOT NULL,
  `nombre` text NOT NULL,
  `apellido` text NOT NULL,
  `genero` int(11) NOT NULL,
  `edad` int(11) NOT NULL,
  `estado` text NOT NULL,
  `municipio` text NOT NULL,
  `temporomaxilar_sintoma` char(1) NOT NULL,
  `temporomaxilar_signo` char(1) NOT NULL,
  `intraoral_lesion` char(1) NOT NULL,
  `intraoral_lesion_localizacion` char(1) DEFAULT NULL,
  `intraoral_lesion_dolor` char(1) DEFAULT NULL,
  `intraoral_lesion_color` char(1) DEFAULT NULL,
  `intraoral_lesion_tamaño` char(1) DEFAULT NULL,
  `intraoral_lesion_consistencia` char(1) DEFAULT NULL,
  `intraoral_lesion_morfologia` char(1) DEFAULT NULL,
  `intraoral_lesion_numero` char(1) DEFAULT NULL,
  `intraoral_flurosis` char(1) NOT NULL,
  `placa_16V` char(1) DEFAULT NULL,
  `placa_11V` char(1) DEFAULT NULL,
  `placa_26V` char(1) DEFAULT NULL,
  `placa_36L` char(1) DEFAULT NULL,
  `placa_31V` char(1) DEFAULT NULL,
  `placa_46L` char(1) DEFAULT NULL,
  `calculo_16V` char(1) DEFAULT NULL,
  `calculo_11V` char(1) DEFAULT NULL,
  `calculo_26V` char(1) DEFAULT NULL,
  `calculo_36L` char(1) DEFAULT NULL,
  `calculo_31V` char(1) DEFAULT NULL,
  `calculo_46L` char(1) DEFAULT NULL,
  `peridontico_0` char(1) DEFAULT NULL,
  `peridontico_1` char(1) DEFAULT NULL,
  `peridontico_2` char(1) DEFAULT NULL,
  `peridontico_3` char(1) DEFAULT NULL,
  `peridontico_4` char(1) DEFAULT NULL,
  `peridontico_5` char(1) DEFAULT NULL,
  `fijacion_0` char(1) DEFAULT NULL,
  `fijacion_1` char(1) DEFAULT NULL,
  `fijacion_2` char(1) DEFAULT NULL,
  `fijacion_3` char(1) DEFAULT NULL,
  `fijacion_4` char(1) DEFAULT NULL,
  `fijacion_5` char(1) DEFAULT NULL,
  `corona_18` char(1) NOT NULL,
  `corona_17` char(1) NOT NULL,
  `corona_16` char(1) NOT NULL,
  `corona_15` char(1) NOT NULL,
  `corona_14` char(1) NOT NULL,
  `corona_13` char(1) NOT NULL,
  `corona_12` char(1) NOT NULL,
  `corona_11` char(1) NOT NULL,
  `tratamiento_18` char(1) NOT NULL,
  `tratamiento_17` char(1) NOT NULL,
  `tratamiento_16` char(1) NOT NULL,
  `tratamiento_15` char(1) NOT NULL,
  `tratamiento_14` char(1) NOT NULL,
  `tratamiento_13` char(1) NOT NULL,
  `tratamiento_12` char(1) NOT NULL,
  `tratamiento_11` char(1) NOT NULL,
  `corona_21` char(1) NOT NULL,
  `corona_22` char(1) NOT NULL,
  `corona_23` char(1) NOT NULL,
  `corona_24` char(1) NOT NULL,
  `corona_25` char(1) NOT NULL,
  `corona_26` char(1) NOT NULL,
  `corona_27` char(1) NOT NULL,
  `corona_28` char(1) NOT NULL,
  `tratamiento_21` char(1) NOT NULL,
  `tratamiento_22` char(1) NOT NULL,
  `tratamiento_23` char(1) NOT NULL,
  `tratamiento_24` char(1) NOT NULL,
  `tratamiento_25` char(1) NOT NULL,
  `tratamiento_26` char(1) NOT NULL,
  `tratamiento_27` char(1) NOT NULL,
  `tratamiento_28` char(1) NOT NULL,
  `corona_48` char(1) NOT NULL,
  `corona_47` char(1) NOT NULL,
  `corona_46` char(1) NOT NULL,
  `corona_45` char(1) NOT NULL,
  `corona_44` char(1) NOT NULL,
  `corona_43` char(1) NOT NULL,
  `corona_42` char(1) NOT NULL,
  `corona_41` char(1) NOT NULL,
  `tratamiento_48` char(1) NOT NULL,
  `tratamiento_47` char(1) NOT NULL,
  `tratamiento_46` char(1) NOT NULL,
  `tratamiento_45` char(1) NOT NULL,
  `tratamiento_44` char(1) NOT NULL,
  `tratamiento_43` char(1) NOT NULL,
  `tratamiento_42` char(1) NOT NULL,
  `tratamiento_41` char(1) NOT NULL,
  `corona_31` char(1) NOT NULL,
  `corona_32` char(1) NOT NULL,
  `corona_33` char(1) NOT NULL,
  `corona_34` char(1) NOT NULL,
  `corona_35` char(1) NOT NULL,
  `corona_36` char(1) NOT NULL,
  `corona_37` char(1) NOT NULL,
  `corona_38` char(1) NOT NULL,
  `tratamiento_31` char(1) NOT NULL,
  `tratamiento_32` char(1) NOT NULL,
  `tratamiento_33` char(1) NOT NULL,
  `tratamiento_34` char(1) NOT NULL,
  `tratamiento_35` char(1) NOT NULL,
  `tratamiento_36` char(1) NOT NULL,
  `tratamiento_37` char(1) NOT NULL,
  `tratamiento_38` char(1) NOT NULL,
  `erupcion_dentaria_17` char(1) DEFAULT NULL,
  `erupcion_dentaria_16` char(1) DEFAULT NULL,
  `erupcion_dentaria_15` char(1) DEFAULT NULL,
  `erupcion_dentaria_14` char(1) DEFAULT NULL,
  `erupcion_dentaria_13` char(1) DEFAULT NULL,
  `erupcion_dentaria_12` char(1) DEFAULT NULL,
  `erupcion_dentaria_11` char(1) DEFAULT NULL,
  `erupcion_dentaria_21` char(1) DEFAULT NULL,
  `erupcion_dentaria_22` char(1) DEFAULT NULL,
  `erupcion_dentaria_23` char(1) DEFAULT NULL,
  `erupcion_dentaria_24` char(1) DEFAULT NULL,
  `erupcion_dentaria_25` char(1) DEFAULT NULL,
  `erupcion_dentaria_26` char(1) DEFAULT NULL,
  `erupcion_dentaria_27` char(1) DEFAULT NULL,
  `erupcion_dentaria_47` char(1) DEFAULT NULL,
  `erupcion_dentaria_46` char(1) DEFAULT NULL,
  `erupcion_dentaria_45` char(1) DEFAULT NULL,
  `erupcion_dentaria_44` char(1) DEFAULT NULL,
  `erupcion_dentaria_43` char(1) DEFAULT NULL,
  `erupcion_dentaria_42` char(1) DEFAULT NULL,
  `erupcion_dentaria_41` char(1) DEFAULT NULL,
  `erupcion_dentaria_31` char(1) DEFAULT NULL,
  `erupcion_dentaria_32` char(1) DEFAULT NULL,
  `erupcion_dentaria_33` char(1) DEFAULT NULL,
  `erupcion_dentaria_34` char(1) DEFAULT NULL,
  `erupcion_dentaria_35` char(1) DEFAULT NULL,
  `erupcion_dentaria_36` char(1) DEFAULT NULL,
  `erupcion_dentaria_37` char(1) DEFAULT NULL,
  `protesis_necesidad_superior` char(1) DEFAULT NULL,
  `protesis_necesidad_inferior` char(1) DEFAULT NULL,
  `protesis_tipo_superior` char(1) NOT NULL,
  `protesis_tipo_inferior` char(1) NOT NULL,
  `habitos_ninguno` char(1) NOT NULL,
  `habitos_bruxismo` char(1) NOT NULL,
  `habitos_respiracion_bucal` char(1) NOT NULL,
  `habitos_deglucion_atipica` char(1) NOT NULL,
  `habitos_succion_digital` char(1) NOT NULL,
  `habitos_no_registrado` char(1) NOT NULL,
  `necesidad_asistencia_trastorno` char(1) NOT NULL,
  `necesidad_asistencia_dolor` char(1) NOT NULL,
  `relacion_oclusion_2dos_molares` char(1) NOT NULL,
  `relacion_oclusion_1ros_molares` char(1) NOT NULL,
  `apiñamiento` char(1) NOT NULL,
  `espaciamiento` char(1) NOT NULL,
  `diastema` char(1) NOT NULL,
  `mordida_abierta` char(1) NOT NULL,
  `morida_profunda_anterior` char(1) NOT NULL,
  `morida_tope_a_tope` char(1) NOT NULL,
  `mordida_cruzada` char(1) NOT NULL,
  `exportado` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `formularios`
--

INSERT INTO `formularios` (`fecha`, `examinador`, `examinador_cedula`, `cedula`, `nombre`, `apellido`, `genero`, `edad`, `estado`, `municipio`, `temporomaxilar_sintoma`, `temporomaxilar_signo`, `intraoral_lesion`, `intraoral_lesion_localizacion`, `intraoral_lesion_dolor`, `intraoral_lesion_color`, `intraoral_lesion_tamaño`, `intraoral_lesion_consistencia`, `intraoral_lesion_morfologia`, `intraoral_lesion_numero`, `intraoral_flurosis`, `placa_16V`, `placa_11V`, `placa_26V`, `placa_36L`, `placa_31V`, `placa_46L`, `calculo_16V`, `calculo_11V`, `calculo_26V`, `calculo_36L`, `calculo_31V`, `calculo_46L`, `peridontico_0`, `peridontico_1`, `peridontico_2`, `peridontico_3`, `peridontico_4`, `peridontico_5`, `fijacion_0`, `fijacion_1`, `fijacion_2`, `fijacion_3`, `fijacion_4`, `fijacion_5`, `corona_18`, `corona_17`, `corona_16`, `corona_15`, `corona_14`, `corona_13`, `corona_12`, `corona_11`, `tratamiento_18`, `tratamiento_17`, `tratamiento_16`, `tratamiento_15`, `tratamiento_14`, `tratamiento_13`, `tratamiento_12`, `tratamiento_11`, `corona_21`, `corona_22`, `corona_23`, `corona_24`, `corona_25`, `corona_26`, `corona_27`, `corona_28`, `tratamiento_21`, `tratamiento_22`, `tratamiento_23`, `tratamiento_24`, `tratamiento_25`, `tratamiento_26`, `tratamiento_27`, `tratamiento_28`, `corona_48`, `corona_47`, `corona_46`, `corona_45`, `corona_44`, `corona_43`, `corona_42`, `corona_41`, `tratamiento_48`, `tratamiento_47`, `tratamiento_46`, `tratamiento_45`, `tratamiento_44`, `tratamiento_43`, `tratamiento_42`, `tratamiento_41`, `corona_31`, `corona_32`, `corona_33`, `corona_34`, `corona_35`, `corona_36`, `corona_37`, `corona_38`, `tratamiento_31`, `tratamiento_32`, `tratamiento_33`, `tratamiento_34`, `tratamiento_35`, `tratamiento_36`, `tratamiento_37`, `tratamiento_38`, `erupcion_dentaria_17`, `erupcion_dentaria_16`, `erupcion_dentaria_15`, `erupcion_dentaria_14`, `erupcion_dentaria_13`, `erupcion_dentaria_12`, `erupcion_dentaria_11`, `erupcion_dentaria_21`, `erupcion_dentaria_22`, `erupcion_dentaria_23`, `erupcion_dentaria_24`, `erupcion_dentaria_25`, `erupcion_dentaria_26`, `erupcion_dentaria_27`, `erupcion_dentaria_47`, `erupcion_dentaria_46`, `erupcion_dentaria_45`, `erupcion_dentaria_44`, `erupcion_dentaria_43`, `erupcion_dentaria_42`, `erupcion_dentaria_41`, `erupcion_dentaria_31`, `erupcion_dentaria_32`, `erupcion_dentaria_33`, `erupcion_dentaria_34`, `erupcion_dentaria_35`, `erupcion_dentaria_36`, `erupcion_dentaria_37`, `protesis_necesidad_superior`, `protesis_necesidad_inferior`, `protesis_tipo_superior`, `protesis_tipo_inferior`, `habitos_ninguno`, `habitos_bruxismo`, `habitos_respiracion_bucal`, `habitos_deglucion_atipica`, `habitos_succion_digital`, `habitos_no_registrado`, `necesidad_asistencia_trastorno`, `necesidad_asistencia_dolor`, `relacion_oclusion_2dos_molares`, `relacion_oclusion_1ros_molares`, `apiñamiento`, `espaciamiento`, `diastema`, `mordida_abierta`, `morida_profunda_anterior`, `morida_tope_a_tope`, `mordida_cruzada`, `exportado`) VALUES
('2025-05-28 12:18:37', 'Jesus', 12345678, 12345678, 'Raul', 'Perez', 1, 12, 'Soltero', '', '1', '2', '1', '2', '2', '2', '3', '4', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '1', '1', '1', '1', '1', '2', '2', '2', '1', '1', '1', '1', '2', '1', '1', '1', '2', '1', '1', '2', '1', '2', '2', '1', '2', '1', '2', '1', '2', '2', '1', '2', '2', '1', '2', '1', '2', '1', '2', '2', '1', '2', '1', '2', '2', '1', '2', '2', '2', '1', '1', '1', '2', '2', '2', '1', '2', '1', '2', '2', '2', '2', '1', '2', '2', '1', '1', '3', '2', '1', '2', '3', '2', '1', '2', '1', '2', '1', '2', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', 0),
('2025-05-28 12:19:07', 'Jesus', 12345678, 87654321, 'Manuel', 'Perez', 1, 34, 'Soltero', '', '1', '2', '1', '2', '2', '2', '3', '4', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '1', '1', '1', '1', '1', '2', '2', '2', '1', '1', '1', '1', '2', '1', '1', '1', '2', '1', '1', '2', '1', '2', '2', '1', '2', '1', '2', '1', '2', '2', '1', '2', '2', '1', '2', '1', '2', '1', '2', '2', '1', '2', '1', '2', '2', '1', '2', '2', '2', '1', '1', '1', '2', '2', '2', '1', '2', '1', '2', '2', '2', '2', '1', '2', '2', '1', '1', '3', '2', '1', '2', '3', '2', '1', '2', '1', '2', '1', '2', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `cedula` int(11) NOT NULL,
  `nombre` text NOT NULL,
  `password` text NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `create_at` datetime NOT NULL DEFAULT current_timestamp(),
  `estado` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`cedula`, `nombre`, `password`, `tipo`, `create_at`, `estado`) VALUES
(1, 'Admin', '356a192b7913b04c54574d18c28d46e6395428ab', 'Administrador', '2025-05-19 14:27:37', 'Activo'),
(28715209, 'Luis Dubuc', '005a36784a0ea769daf5ccfae4e48a2b8f6445eb', 'Examinador', '2025-05-19 14:27:37', 'Activo'),
(29929976, 'Samuel Chourio', '81d5535086e184733bcb8a7974afd3b3393f5723', 'Administrador', '2025-05-27 10:09:52', 'Activo');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`cedula`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
