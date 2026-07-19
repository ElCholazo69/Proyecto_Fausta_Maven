CREATE DATABASE  IF NOT EXISTS `panaderiafausta` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `panaderiafausta`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: panaderiafausta
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ciudad`
--

DROP TABLE IF EXISTS `ciudad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ciudad` (
  `id_ciudad` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_ciudad`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ciudad`
--

LOCK TABLES `ciudad` WRITE;
/*!40000 ALTER TABLE `ciudad` DISABLE KEYS */;
INSERT INTO `ciudad` VALUES (1,'Lima'),(2,'Ica'),(3,'Ayacucho'),(4,'Arequipa'),(5,'Ancash');
/*!40000 ALTER TABLE `ciudad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(40) DEFAULT NULL,
  `apellidos` varchar(40) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `contrasena` varchar(25) DEFAULT NULL,
  `telefono` varchar(9) DEFAULT NULL,
  `dni` varchar(8) DEFAULT NULL,
  `id_ciudad` int DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `dni_UNIQUE` (`dni`),
  UNIQUE KEY `telefono_UNIQUE` (`telefono`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_cliente_ciudad_idx` (`id_ciudad`),
  CONSTRAINT `fk_cliente_ciudad` FOREIGN KEY (`id_ciudad`) REFERENCES `ciudad` (`id_ciudad`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'Aron','Berch','aron01@gmail.com','aron123','900349251','12345678',1),(2,'Pedro','Ramirez','pedro01@gmail.com','1234','906940428','86439677',3),(3,'Ana','Piedras','ana01@gmail.com','1234','977344122','88764356',4),(4,'Juan','Guerrero','juan01@gmail.com','1234','990093111','29381021',2),(5,'Carlos','Morales','carlos01@gmail.com','1234','902344719','45331436',3),(6,'Pepe','Ramirez','pepe01@gmail.com','1234','985112448','89374813',5),(7,'Luis','Lopez','luis01@gmail.com','1234','952145889','88422012',1),(8,'Alejandro','Diaz','alejandro01@gmail.com','1234','998934927','99884123',3),(9,'Douglas','Lopez','douglas01@gmail.com','1234','499543511','99884414',4),(10,'Felipe','Lobos','Felipe01@gmail.com','123','325235235','21312467',5),(11,'Oliver','Perez','oliver01@gmail.com','1234','123456789','87676576',5),(12,'Pepito','Lalo','pepito01@gmail.com','1234','343243248','75848753',1),(13,'Enzo','Mayaute','enzo01@gmail.com','1234','656345435','54353454',3),(14,'David','Gutierrez','david01@gmail.com','1234','987547534','76438201',1);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consulta`
--

DROP TABLE IF EXISTS `consulta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consulta` (
  `id_consulta` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int DEFAULT NULL,
  `asunto` varchar(60) DEFAULT NULL,
  `motivo` text,
  PRIMARY KEY (`id_consulta`),
  KEY `fk_consulta_cliente_idx` (`id_cliente`),
  CONSTRAINT `fk_consulta_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consulta`
--

LOCK TABLES `consulta` WRITE;
/*!40000 ALTER TABLE `consulta` DISABLE KEYS */;
INSERT INTO `consulta` VALUES (4,1,'Consulta de producto','Nose'),(5,1,'Sugerencia','¿Aceptan yape?'),(6,1,'Reclamo','Mi pedido vino con retraso.'),(7,1,'Problema con pago','No me deja pagar con mi tarjeta de credito.');
/*!40000 ALTER TABLE `consulta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descuento`
--

DROP TABLE IF EXISTS `descuento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descuento` (
  `id_descuento` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(6) DEFAULT NULL,
  `descuento` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_descuento`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descuento`
--

LOCK TABLES `descuento` WRITE;
/*!40000 ALTER TABLE `descuento` DISABLE KEYS */;
INSERT INTO `descuento` VALUES (1,'ABC123',0.10),(2,'DESCUE',0.20),(3,'PALOMA',0.50);
/*!40000 ALTER TABLE `descuento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detallepedido`
--

DROP TABLE IF EXISTS `detallepedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detallepedido` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `id_pedido` int DEFAULT NULL,
  `id_producto` int DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `tamano` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_detalle`),
  KEY `fk_detalle_pedido_idx` (`id_pedido`),
  KEY `fk_detalle_producto_idx` (`id_producto`),
  CONSTRAINT `fk_detalle_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedido` (`id_pedido`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_detalle_producto` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id_producto`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detallepedido`
--

LOCK TABLES `detallepedido` WRITE;
/*!40000 ALTER TABLE `detallepedido` DISABLE KEYS */;
INSERT INTO `detallepedido` VALUES (69,1,1,2,100.00,'Mediano'),(70,1,6,1,12.00,'Pequeño'),(71,1,13,3,27.00,'Pequeño'),(72,1,18,1,180.00,'x100unid'),(73,1,23,2,28.00,'6 uni'),(74,2,11,2,18.00,'Pequeño'),(75,2,25,3,18.00,'Individual'),(76,2,8,1,45.00,'Mediano'),(77,3,2,2,100.00,'Mediano'),(78,3,7,2,28.00,'Pequeño'),(79,3,14,1,180.00,'x100unid'),(80,3,28,2,100.00,'Individual'),(81,4,1,3,45.00,'Pequeño'),(82,4,3,1,70.00,'Grande'),(83,5,5,2,20.00,'Pequeño'),(84,5,9,1,0.00,'Mediano'),(85,5,10,3,24.00,'Pequeño'),(86,5,26,2,12.00,'Individual'),(89,7,1,1,50.00,'Mediano'),(90,8,2,1,50.00,'Mediano'),(91,9,6,4,48.00,'Pequeño');
/*!40000 ALTER TABLE `detallepedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(40) DEFAULT NULL,
  `apellidos` varchar(40) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `contrasena` varchar(25) DEFAULT NULL,
  `telefono` varchar(9) DEFAULT NULL,
  PRIMARY KEY (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (1,'Admin','Valdez','admin1@gmail.com','admin','785436789');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `id_pedido` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int DEFAULT NULL,
  `id_empleado` int DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `estado` varchar(10) DEFAULT NULL,
  `metodo_envio` varchar(40) DEFAULT NULL,
  `direccion` text,
  `id_descuento` int DEFAULT NULL,
  PRIMARY KEY (`id_pedido`),
  KEY `fk_pedido_empleado_idx` (`id_empleado`) /*!80000 INVISIBLE */,
  KEY `fk_pedido_cliente_idx` (`id_cliente`),
  KEY `fk_pedido_descuento_idx` (`id_descuento`),
  CONSTRAINT `fk_pedido_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_pedido_descuento` FOREIGN KEY (`id_descuento`) REFERENCES `descuento` (`id_descuento`) ON DELETE CASCADE,
  CONSTRAINT `fk_pedido_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,2,1,'2026-02-28','Finalizado','tienda',NULL,NULL),(2,2,1,'2026-02-28','Finalizado','tienda',NULL,NULL),(3,2,1,'2026-02-28','Finalizado','tienda',NULL,NULL),(4,2,1,'2026-05-05','Pendiente','tienda',NULL,NULL),(5,14,1,'2026-05-07','Finalizado','tienda',NULL,NULL),(7,14,1,'2026-05-07','Cancelado','tienda',NULL,NULL),(8,14,1,'2026-05-07','Cancelado','tienda',NULL,NULL),(9,14,1,'2026-05-07','Finalizado','tienda',NULL,NULL);
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(101) DEFAULT NULL,
  `descripcion` text,
  `precio1` decimal(10,2) DEFAULT NULL,
  `precio2` decimal(10,2) DEFAULT NULL,
  `precio3` decimal(10,2) DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `categoria` varchar(20) DEFAULT NULL,
  `imagen` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,'Torta de chocolate con Extra Fudge','Elaborado con los cacaos más deliciosos',15.00,50.00,85.00,176,'tortas','torta_chocolate1.jpg'),(2,'Torta de Pecanas','Dulce torta crujiente con nueces',15.00,50.00,85.00,75,'tortas','torta_pecana.jpg'),(3,'Torta de Carrot Cake','Carrot cake dulce, esponjoso y delicioso',14.00,45.00,70.00,63,'tortas','torta_zanahoria1.jpg'),(4,'Pye De Limón','Dulce, ácido, cremoso y refrescante postre',13.00,0.00,0.00,44,'tortas','torta_pielimon.jpg'),(5,'Crocante de lúcuma','Dulce crunch cremoso sabor lúcuma',10.00,0.00,65.00,32,'tortas','torta_crocantelucuma.jpg'),(6,'Pye de Manzana','Dulce tarta de manzana casera',12.00,0.00,0.00,60,'tortas','torta_piemanzana.jpg'),(7,'Keke de Manzana con Butterscotch','Dulce keke de manzana irresistible',14.00,0.00,0.00,54,'tortas','torta_kekemanzana.png'),(8,'Tarta de Pecanas','Dulce, crujiente, cremosa y deliciosa pecana',13.00,45.00,0.00,87,'tortas','torta_tartapecana1.jpg'),(9,'Mini Tortas','Tortas pequeñas, dulces y decoradas',0.00,70.00,0.00,48,'tortas','torta_mini2.jpg'),(10,'Galleta Avena','Dulce, crujiente, nutritiva, saludable',8.00,0.00,0.00,84,'galletones','galletones_avena1.png'),(11,'Galleta con líneas de chocolate','Chocolateada, deliciosa, reconfortante',9.00,0.00,0.00,50,'galletones','galletones_chocolate.png'),(12,'Galleta Nutella','Crujiente, chocolate, dulce',9.00,0.00,0.00,38,'galletones','galletones_nutella.png'),(13,'Galleta Red Velvet','Galleta roja, suave y irresistible',9.00,0.00,0.00,32,'galletones','galletones_redvelvet.jpg'),(14,'Mini Brownies','Pequeños, chocolatosos, irresistibles',180.00,0.00,0.00,37,'bocaditos','bocaditos_brownie.png'),(15,'Mini Alfajores','Alfajores, rellenos, caramelizados',180.00,0.00,0.00,58,'bocaditos','bocaditos_minialfajor.png'),(16,'Mini cupcakes','Cupcakes, tiernos, glaseados',200.00,0.00,0.00,45,'bocaditos','bocaditos_cupcake.png'),(17,'Mini Guargueros','Rellenos, crujientes, tradicionales',180.00,0.00,0.00,55,'bocaditos','bocaditos_miniguarguejos.png'),(18,'Truffas','Cremosas, chocolatosas, gourmet',180.00,0.00,0.00,68,'bocaditos','bocaditos_truffas.png'),(19,'Mini Macarrones','Pequeños, dulces, coloridos',250.00,0.00,0.00,40,'bocaditos','bocaditos_macarones.png'),(20,'Empanada De Carne','Crujiente rellena de carne sazonada',8.00,20.00,0.00,74,'bocaditos','bocaditos_empanadaCarne.png'),(21,'Empanada De Pollo','Deliciosa, jugosa, sazonada',8.00,20.00,0.00,45,'bocaditos','bocaditos_empanadaPollo.png'),(22,'Empanada De Pizza','Crujiente, sabrosa, con queso',8.00,20.00,0.00,63,'bocaditos','bocaditos_empanadaPizza.png'),(23,'Alfajores de pecanas','Dulce, crujiente, relleno, nuez',14.00,28.00,56.00,93,'dulces','dulce_alfajorPecana.png'),(24,'King Kong','Dulce, gigante, relleno',14.00,0.00,0.00,74,'dulces','dulce_kingkong.jpg'),(25,'Limón de Convento','Ácido, aromático, jugoso',6.00,0.00,0.00,43,'dulces','dulce_limonconvento.jpg'),(26,'Prestiño Huanuqueño','Manjar, tradicional, exquisito',6.00,0.00,0.00,88,'dulces','dulce_prestinio.jpg'),(27,'Suspiro de Limeña','Dulce, cremoso, suave',7.00,12.00,0.00,56,'dulces','dulce_suspirolimenia.jpg'),(28,'Turrón de Doña Pepa','Dulce, tradicional, crujiente',50.00,0.00,0.00,82,'dulces','dulce_turronPepa1.jpeg');
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'panaderiafausta'
--

--
-- Dumping routines for database 'panaderiafausta'
--
/*!50003 DROP PROCEDURE IF EXISTS `ActualizarPerfil` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `ActualizarPerfil`(
	IN p_id_cliente INT,
    IN p_nombres VARCHAR(40),
    IN p_apellidos VARCHAR(40),
    IN p_email VARCHAR(50),
    IN p_telefono VARCHAR(9),
    IN p_dni VARCHAR(8),
    IN p_id_ciudad INT
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    -- Validación de que el cliente exista
    SELECT COUNT(*) INTO v_existe
    FROM cliente
    WHERE id_cliente = p_id_cliente;
    
    IF v_existe = 0 THEN
        SELECT 'ERROR' AS estado, 'El cliente no existe' AS mensaje;

    ELSE
        -- Validación de EMAIL único
        SELECT COUNT(*) INTO v_existe
        FROM cliente
        WHERE email = p_email
        AND id_cliente <> p_id_cliente;

        IF v_existe > 0 THEN
            SELECT 'ERROR' AS estado, 'El email ingresado ya está registrado' AS mensaje;

        ELSE
            -- Validación de DNI
            SELECT COUNT(*) INTO v_existe
            FROM cliente
            WHERE dni = p_dni
            AND id_cliente <> p_id_cliente;

            IF v_existe > 0 THEN
                SELECT 'ERROR' AS estado, 'El DNI ingresado ya está registrado' AS mensaje;

            ELSE
                -- Validación de teléfono único
                SELECT COUNT(*) INTO v_existe
                FROM cliente
                WHERE telefono = p_telefono
                AND id_cliente <> p_id_cliente;

                IF v_existe > 0 THEN
                    SELECT 'ERROR' AS estado, 'El teléfono ingresado ya está registrado' AS mensaje;

                ELSE
                    -- Actualizar datos
                    UPDATE cliente
                    SET nombres = p_nombres,
                        apellidos = p_apellidos,
                        email = p_email,
                        telefono = p_telefono,
                        dni = p_dni,
                        id_ciudad = p_id_ciudad
                    WHERE id_cliente = p_id_cliente;

                    SELECT 'OK' AS estado, 'Datos actualizados correctamente' AS mensaje;

                END IF;
            END IF;
        END IF;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertarCliente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `InsertarCliente`(
	IN p_nombres VARCHAR(40),
    IN p_apellidos VARCHAR(40),
    IN p_email VARCHAR(50),
    IN p_contrasena VARCHAR(25),
    IN p_telefono VARCHAR(9),
    IN p_dni VARCHAR(8),
    IN p_id_ciudad INT)
BEGIN
	DECLARE v_existe_dni INT default 0;
    DECLARE v_existe_tel INT default 0;
    DECLARE v_existe_email INT default 0;
    
    SELECT COUNT(*) INTO v_existe_email FROM cliente WHERE email=p_email;
    SELECT COUNT(*) INTO v_existe_tel FROM cliente WHERE telefono=p_telefono;
    SELECT COUNT(*) INTO v_existe_dni FROM cliente WHERE dni=p_dni;
    
    IF v_existe_email > 0 THEN
		SELECT 'ERROR' AS estado, 'El correo ya está registrado' AS mensaje;
	ELSEIF v_existe_tel > 0 THEN
		SELECT 'ERROR' AS estado, 'El telefono ya está registrado' AS mensaje;
    ELSEIF v_existe_dni > 0 THEN
		SELECT 'ERROR' AS estado, 'El DNI ya está registrado' AS mensaje;
	ELSE
		INSERT INTO cliente(nombres,apellidos,email,contrasena,telefono,dni,id_ciudad)
		VALUES (p_nombres,p_apellidos,p_email,p_contrasena,p_telefono,p_dni,p_id_ciudad);
        
        SELECT 'OK' AS estado, 'Cliente registrado correctamente' AS mensaje;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `InsertarConsulta` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `InsertarConsulta`(
	IN p_id_cliente INT,
    IN p_asunto VARCHAR(60),
    IN p_motivo TEXT)
BEGIN
	INSERT INTO consulta (id_cliente,asunto,motivo)
    VALUES (p_id_cliente,p_asunto,p_motivo);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ListarClientes` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `ListarClientes`()
BEGIN
	SELECT * FROM cliente;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ListarConsulta` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `ListarConsulta`()
BEGIN
	SELECT * FROM consulta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ListarProductos` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `ListarProductos`()
BEGIN
	SELECT imagen,
    nombre,
    descripcion,
    precio1,
    precio2,
    precio3,
    categoria,
    stock FROM producto;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LoginUsuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `LoginUsuario`(
	IN p_identificador VARCHAR(50), -- Puede ser correo o DNI
	IN p_contrasena VARCHAR(25)
    )
BEGIN
	DECLARE v_id INT DEFAULT NULL;
	DECLARE v_nombre VARCHAR(40);
    DECLARE v_apellido VARCHAR(40);
	DECLARE v_email VARCHAR(50);
	DECLARE v_pwd VARCHAR(25);
	DECLARE v_tipo VARCHAR(20);
	DECLARE v_telefono VARCHAR(9);
	DECLARE v_dni VARCHAR(8);
    DECLARE v_id_ciudad INT;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_id = NULL;

	-- Buscar en la tabla cliente (correo o DNI)
	SELECT id_cliente, nombres,apellidos, email, contrasena, telefono, dni, id_ciudad
	INTO v_id, v_nombre, v_apellido, v_email, v_pwd, v_telefono, v_dni, v_id_ciudad
	FROM cliente
	WHERE email = p_identificador OR dni = p_identificador
	LIMIT 1;

	IF v_id IS NOT NULL THEN
		-- Cliente encontrado
		SET v_tipo = 'cliente';
		IF v_pwd = p_contrasena THEN
			SELECT 'OK' AS estado, v_tipo AS tipo, v_id AS id, v_nombre AS nombres, v_apellido AS apellidos,
			       v_email AS email, v_telefono AS telefono, v_dni AS dni,
                   v_id_ciudad AS id_ciudad, NULL AS mensaje;
		ELSE
			-- Diferenciar si fue correo o DNI
			IF v_email = p_identificador THEN
				SELECT 'ERROR_CONTRASENA' AS estado, NULL AS tipo, NULL AS id, NULL AS nombres, NULL AS apellidos,
				       NULL AS email, NULL AS telefono, NULL AS dni, NULL AS id_ciudad,
				       'Correo del usuario existe pero la contraseña es incorrecta' AS mensaje;
			ELSE
				SELECT 'ERROR_CONTRASENA' AS estado, NULL AS tipo, NULL AS id, NULL AS nombres, NULL AS apellidos,
				       NULL AS email, NULL AS telefono, NULL AS dni, NULL AS id_ciudad,
				       'DNI del usuario existe pero la contraseña es incorrecta' AS mensaje;
			END IF;
		END IF;
	ELSE
		-- No es cliente, buscar en la tabla empleado
		SELECT id_empleado, nombres, apellidos, email, contrasena
		INTO v_id, v_nombre, v_apellido, v_email, v_pwd
		FROM empleado
		WHERE email = p_identificador
		LIMIT 1;

		IF v_id IS NOT NULL THEN
			IF v_pwd = p_contrasena THEN
				SET v_tipo = 'empleado';
				SELECT 'OK' AS estado, v_tipo AS tipo, v_id AS id, v_nombre AS nombres, v_apellido AS apellidos,
				       v_email AS email, NULL AS telefono, NULL AS dni, NULL AS id_ciudad,
				       NULL AS mensaje;
			ELSE
				SELECT 'ERROR_CONTRASENA' AS estado, NULL AS tipo, NULL AS id, NULL AS nombres, NULL AS apellidos,
				       NULL AS email, NULL AS telefono, NULL AS dni, NULL AS id_ciudad,
				       'Contraseña incorrecta (empleado)' AS mensaje;
			END IF;
		ELSE
			SELECT 'NO_EXISTE' AS estado, NULL AS tipo, NULL AS id, NULL AS nombres, NULL AS apellidos,
			       NULL AS email, NULL AS telefono, NULL AS dni, NULL AS id_ciudad,
			       'Usuario no existe' AS mensaje;
		END IF;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-08 20:13:13
