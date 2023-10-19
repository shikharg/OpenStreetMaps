-- phpMyAdmin SQL Dump
-- version 3.5.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 20, 2014 at 04:48 AM
-- Server version: 5.5.29
-- PHP Version: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `CarData`
--

-- --------------------------------------------------------

--
-- Table structure for table `TrafficData`
--
CREATE DATABASE `CarData`;
CREATE TABLE `TrafficData` (
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `average_number_of_cars` double NOT NULL,
  `average_speed_of_cars` double NOT NULL,
  `freeway` varchar(50) CHARACTER SET ascii NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
