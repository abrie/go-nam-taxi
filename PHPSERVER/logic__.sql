-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 08, 2015 at 10:45 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `logic`
--

-- --------------------------------------------------------

--
-- Table structure for table `card`
--

CREATE TABLE IF NOT EXISTS `card` (
  `pass_id` int(11) NOT NULL,
  `card_id` varchar(50) NOT NULL,
  `pin` varchar(50) NOT NULL,
  `date` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `owners`
--

CREATE TABLE IF NOT EXISTS `owners` (
`owner_id` int(11) NOT NULL,
  `fname` varchar(50) NOT NULL,
  `lname` varchar(50) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_no` varchar(12) NOT NULL,
  `city` varchar(50) NOT NULL,
  `added_by` int(11) NOT NULL,
  `pin` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `owners`
--

INSERT INTO `owners` (`owner_id`, `fname`, `lname`, `date`, `id_no`, `city`, `added_by`, `pin`) VALUES
(9, 'Jonas', 'Marshal', '2015-10-05 10:31:54', '91010200127', 'Windhoek', 2, '482bd57ea95bb42cc15c82d63af42ea9');

-- --------------------------------------------------------

--
-- Table structure for table `pass_notifications`
--

CREATE TABLE IF NOT EXISTS `pass_notifications` (
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pass_id` int(11) NOT NULL,
  `seat` varchar(10) NOT NULL,
  `amount` float NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `taxi_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pass_notifications`
--

INSERT INTO `pass_notifications` (`date`, `pass_id`, `seat`, `amount`, `status`, `taxi_id`) VALUES
('2015-10-05 00:43:57', 1, '2', 20, 1, 1),
('2015-10-05 00:54:34', 1, '1', 20, 1, 1),
('2015-10-05 11:31:28', 1, '3', 20, 1, 1),
('2015-10-05 12:11:11', 1, '3', 20, 1, 1),
('2015-10-05 12:15:08', 1, '3', 20, 1, 1),
('2015-10-05 12:15:59', 1, '3', 20, 1, 1),
('2015-10-05 12:20:43', 1, '3', 20, 0, 1),
('2015-10-05 12:26:31', 1, '3', 20, 0, 1),
('2015-10-05 12:28:24', 1, '3', 20, 0, 1),
('2015-10-05 12:44:02', 1, '3', 20, 0, 1),
('2015-10-05 12:48:07', 1, '3', 20, 0, 1),
('2015-10-05 12:52:52', 1, '3', 20, 0, 1),
('2015-10-05 14:21:03', 1, '3', 20, 0, 1),
('2015-10-05 14:58:54', 1, '3', 20, 0, 1),
('2015-10-07 16:01:07', 1, '2', 10, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `passenger_accounts`
--

CREATE TABLE IF NOT EXISTS `passenger_accounts` (
`trans_id` int(11) NOT NULL,
  `tdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `taxi_id` int(11) NOT NULL,
  `method` varchar(25) NOT NULL,
  `debit` float NOT NULL DEFAULT '0',
  `credit` float NOT NULL DEFAULT '0',
  `pass_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `passenger_accounts`
--

INSERT INTO `passenger_accounts` (`trans_id`, `tdate`, `taxi_id`, `method`, `debit`, `credit`, `pass_id`) VALUES
(1, '2015-10-05 00:04:17', 1, 'USSD', 200, 0, 1),
(2, '2015-10-05 00:05:07', 1, 'USSD', 200, 0, 1),
(3, '2015-10-05 00:05:25', 1, 'USSD', 200, 0, 1),
(4, '2015-10-05 00:09:11', 1, 'USSD', 20, 0, 1),
(5, '2015-10-05 00:11:43', 1, 'USSD', 20, 0, 1),
(6, '2015-10-05 00:12:37', 1, 'USSD', 20, 0, 1),
(7, '2015-10-05 00:14:08', 1, 'USSD', 20, 0, 1),
(8, '2015-10-05 00:20:32', 1, 'USSD', 20, 0, 1),
(9, '2015-10-05 00:20:33', 1, 'USSD', 20, 0, 1),
(10, '2015-10-05 00:33:44', 1, 'USSD', 20, 0, 1),
(11, '2015-10-05 00:34:46', 1, 'USSD', 20, 0, 1),
(12, '2015-10-05 00:34:49', 1, 'USSD', 20, 0, 1),
(13, '2015-10-05 00:34:50', 1, 'USSD', 20, 0, 1),
(14, '2015-10-05 00:34:51', 1, 'USSD', 20, 0, 1),
(15, '2015-10-05 00:35:32', 1, 'USSD', 20, 0, 1),
(16, '2015-10-05 00:35:33', 1, 'USSD', 20, 0, 1),
(17, '2015-10-05 00:36:31', 1, 'USSD', 20, 0, 1),
(18, '2015-10-05 00:36:32', 1, 'USSD', 20, 0, 1),
(19, '2015-10-05 00:36:33', 1, 'USSD', 20, 0, 1),
(20, '2015-10-05 00:36:34', 1, 'USSD', 20, 0, 1),
(21, '2015-10-05 00:36:35', 1, 'USSD', 20, 0, 1),
(22, '2015-10-05 00:36:36', 1, 'USSD', 20, 0, 1),
(23, '2015-10-05 00:36:37', 1, 'USSD', 20, 0, 1),
(24, '2015-10-05 00:36:38', 1, 'USSD', 20, 0, 1),
(25, '2015-10-05 00:36:39', 1, 'USSD', 20, 0, 1),
(26, '2015-10-05 00:36:40', 1, 'USSD', 20, 0, 1),
(27, '2015-10-05 00:36:42', 1, 'USSD', 20, 0, 1),
(28, '2015-10-05 00:36:43', 1, 'USSD', 20, 0, 1),
(29, '2015-10-05 00:41:49', 1, 'USSD', 20, 0, 1),
(30, '2015-10-05 00:41:50', 1, 'USSD', 20, 0, 1),
(31, '2015-10-05 00:42:05', 1, 'USSD', 20, 0, 1),
(32, '2015-10-05 00:43:30', 1, 'USSD', 20, 0, 1),
(33, '2015-10-05 00:43:34', 1, 'USSD', 20, 0, 1),
(34, '2015-10-05 00:43:35', 1, 'USSD', 20, 0, 1),
(35, '2015-10-05 00:43:57', 1, 'USSD', 20, 0, 1),
(36, '2015-10-05 00:54:34', 1, 'USSD', 20, 0, 1),
(37, '2015-10-05 11:31:28', 1, 'USSD', 20, 0, 1),
(38, '2015-10-05 12:11:11', 1, 'USSD', 20, 0, 1),
(39, '2015-10-05 12:15:08', 1, 'USSD', 20, 0, 1),
(40, '2015-10-05 12:15:59', 1, 'USSD', 20, 0, 1),
(41, '2015-10-05 12:20:43', 1, 'USSD', 20, 0, 1),
(42, '2015-10-05 12:26:31', 1, 'USSD', 20, 0, 1),
(43, '2015-10-05 12:28:24', 1, 'USSD', 20, 0, 1),
(44, '2015-10-05 12:44:02', 1, 'USSD', 20, 0, 1),
(45, '2015-10-05 12:48:07', 1, 'USSD', 20, 0, 1),
(46, '2015-10-05 12:52:51', 1, 'USSD', 20, 0, 1),
(47, '2015-10-05 14:21:03', 1, 'USSD', 20, 0, 1),
(48, '2015-10-05 14:58:54', 1, 'USSD', 20, 0, 1),
(49, '2015-10-07 16:01:06', 1, 'USSD', 10, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `passenger_balances`
--

CREATE TABLE IF NOT EXISTS `passenger_balances` (
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `pass_id` int(11) NOT NULL,
  `total_credit` float NOT NULL,
  `total_debit` float NOT NULL,
  `balance` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `passenger_balances`
--

INSERT INTO `passenger_balances` (`date`, `pass_id`, `total_credit`, `total_debit`, `balance`) VALUES
('2015-10-07 16:01:07', 1, 0, 1110, 75);

-- --------------------------------------------------------

--
-- Table structure for table `passengers`
--

CREATE TABLE IF NOT EXISTS `passengers` (
`pass_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fname` varchar(50) NOT NULL,
  `lname` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `id_no` varchar(12) NOT NULL,
  `prime_contact` varchar(12) NOT NULL,
  `sec_contact` varchar(12) DEFAULT NULL,
  `added_by` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `passengers`
--

INSERT INTO `passengers` (`pass_id`, `date`, `fname`, `lname`, `city`, `id_no`, `prime_contact`, `sec_contact`, `added_by`) VALUES
(1, '2015-10-04 14:44:02', 'Leroy', 'Diocotlhe', 'Windhoek', '2147483647', '0852221234', 'dio@iway.na', 0),
(2, '2015-10-05 11:13:46', 'Jonas', 'Marshal', 'Windhoek', '91010200127', '+26481328702', NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `session`
--

CREATE TABLE IF NOT EXISTS `session` (
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mobile_number` varchar(30) NOT NULL,
  `taxi_id` int(11) NOT NULL,
  `amount` float NOT NULL,
  `seats` int(11) NOT NULL,
  `stage` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `taxi_accounts`
--

CREATE TABLE IF NOT EXISTS `taxi_accounts` (
  `trans_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `taxi_id` int(11) NOT NULL,
  `method` varchar(25) NOT NULL,
  `debit` float NOT NULL,
  `credit` float NOT NULL,
  `pass_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `taxi_accounts`
--

INSERT INTO `taxi_accounts` (`trans_id`, `date`, `taxi_id`, `method`, `debit`, `credit`, `pass_id`) VALUES
(0, '2015-10-05 00:09:11', 1, 'USSD', 0, 20, 1),
(35, '2015-10-05 00:43:57', 1, 'USSD', 0, 20, 1),
(36, '2015-10-05 00:54:34', 1, 'USSD', 0, 20, 1),
(37, '2015-10-05 11:31:28', 1, 'USSD', 0, 20, 1),
(38, '2015-10-05 12:11:11', 1, 'USSD', 0, 20, 1),
(39, '2015-10-05 12:15:08', 1, 'USSD', 0, 20, 1),
(40, '2015-10-05 12:15:59', 1, 'USSD', 0, 20, 1),
(41, '2015-10-05 12:20:43', 1, 'USSD', 0, 20, 1),
(42, '2015-10-05 12:26:31', 1, 'USSD', 0, 20, 1),
(43, '2015-10-05 12:28:24', 1, 'USSD', 0, 20, 1),
(44, '2015-10-05 12:44:02', 1, 'USSD', 0, 20, 1),
(45, '2015-10-05 12:48:07', 1, 'USSD', 0, 20, 1),
(46, '2015-10-05 12:52:51', 1, 'USSD', 0, 20, 1),
(47, '2015-10-05 14:21:03', 1, 'USSD', 0, 20, 1),
(48, '2015-10-05 14:58:54', 1, 'USSD', 0, 20, 1),
(49, '2015-10-07 16:01:07', 1, 'USSD', 0, 10, 1);

-- --------------------------------------------------------

--
-- Table structure for table `taxi_balances`
--

CREATE TABLE IF NOT EXISTS `taxi_balances` (
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `taxi_id` int(11) NOT NULL,
  `total_credit` float NOT NULL,
  `total_debit` float NOT NULL,
  `balance` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `taxi_balances`
--

INSERT INTO `taxi_balances` (`date`, `taxi_id`, `total_credit`, `total_debit`, `balance`) VALUES
('2015-10-07 16:01:07', 1, 310, 0, 610);

-- --------------------------------------------------------

--
-- Table structure for table `taxi_notifications`
--

CREATE TABLE IF NOT EXISTS `taxi_notifications` (
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pass_id` int(11) NOT NULL DEFAULT '0',
  `seat` varchar(10) NOT NULL,
  `amount` float NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `taxi_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `taxi_notifications`
--

INSERT INTO `taxi_notifications` (`date`, `pass_id`, `seat`, `amount`, `status`, `taxi_id`) VALUES
('2015-10-07 16:01:07', 0, '2', 10, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `transid`
--

CREATE TABLE IF NOT EXISTS `transid` (
  `year` year(4) NOT NULL,
  `month` int(11) NOT NULL,
  `value` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
`user_id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `user_type` varchar(15) NOT NULL DEFAULT 'admin',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `user_type`, `date`, `password`) VALUES
(3, 'Celleb', 'admin', '2015-10-05 11:21:44', 'c03ef18feb1826d59970b9d53983abc0'),
(4, 'leroy', 'admin', '2015-10-05 11:22:16', '6ee4712fff8e386ffa75e6195f96dcf2 ');

-- --------------------------------------------------------

--
-- Table structure for table `ussd`
--

CREATE TABLE IF NOT EXISTS `ussd` (
  `pass_id` int(11) NOT NULL,
  `mobile_numb` varchar(13) NOT NULL,
  `pin` varchar(50) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ussd`
--

INSERT INTO `ussd` (`pass_id`, `mobile_numb`, `pin`, `date`, `status`) VALUES
(2, '+264813287027', '482bd57ea95bb42cc15c82d63af42ea9', '2015-10-05 11:13:46', 0),
(1, '0853478687', 'df104bad5b290fded5941b63985b96c5', '2015-10-04 16:25:58', 0);

-- --------------------------------------------------------

--
-- Table structure for table `vehicles`
--

CREATE TABLE IF NOT EXISTS `vehicles` (
`taxi_id` int(11) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `seats` int(11) NOT NULL,
  `car_reg` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `nplate` varchar(20) NOT NULL,
  `taxi_code` varchar(5) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vehicles`
--

INSERT INTO `vehicles` (`taxi_id`, `owner_id`, `date`, `seats`, `car_reg`, `model`, `nplate`, `taxi_code`) VALUES
(1, 1, '2015-10-04', 4, 'AXASOMETHING', 'TOYOTA Corola', 'N1352536W', '12'),
(2, 9, '0000-00-00', 3, '344q6t346366', 'corolla', 'N34466', 'a345');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `card`
--
ALTER TABLE `card`
 ADD PRIMARY KEY (`card_id`), ADD UNIQUE KEY `pass_id` (`pass_id`);

--
-- Indexes for table `owners`
--
ALTER TABLE `owners`
 ADD PRIMARY KEY (`owner_id`), ADD UNIQUE KEY `id_no` (`id_no`);

--
-- Indexes for table `pass_notifications`
--
ALTER TABLE `pass_notifications`
 ADD PRIMARY KEY (`date`);

--
-- Indexes for table `passenger_accounts`
--
ALTER TABLE `passenger_accounts`
 ADD PRIMARY KEY (`trans_id`);

--
-- Indexes for table `passenger_balances`
--
ALTER TABLE `passenger_balances`
 ADD PRIMARY KEY (`pass_id`);

--
-- Indexes for table `passengers`
--
ALTER TABLE `passengers`
 ADD PRIMARY KEY (`pass_id`);

--
-- Indexes for table `session`
--
ALTER TABLE `session`
 ADD PRIMARY KEY (`mobile_number`);

--
-- Indexes for table `taxi_accounts`
--
ALTER TABLE `taxi_accounts`
 ADD PRIMARY KEY (`trans_id`);

--
-- Indexes for table `taxi_balances`
--
ALTER TABLE `taxi_balances`
 ADD PRIMARY KEY (`taxi_id`);

--
-- Indexes for table `taxi_notifications`
--
ALTER TABLE `taxi_notifications`
 ADD PRIMARY KEY (`taxi_id`);

--
-- Indexes for table `transid`
--
ALTER TABLE `transid`
 ADD PRIMARY KEY (`year`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`user_id`), ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `ussd`
--
ALTER TABLE `ussd`
 ADD PRIMARY KEY (`mobile_numb`);

--
-- Indexes for table `vehicles`
--
ALTER TABLE `vehicles`
 ADD PRIMARY KEY (`taxi_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `owners`
--
ALTER TABLE `owners`
MODIFY `owner_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `passenger_accounts`
--
ALTER TABLE `passenger_accounts`
MODIFY `trans_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=50;
--
-- AUTO_INCREMENT for table `passengers`
--
ALTER TABLE `passengers`
MODIFY `pass_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `vehicles`
--
ALTER TABLE `vehicles`
MODIFY `taxi_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
