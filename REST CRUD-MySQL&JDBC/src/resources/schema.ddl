create database issue_report;
use issue_report;
create table reports(id int AUTO_INCREMENT,published_date varchar(255),PRIMARY KEY(id), issue_reported varchar(255), address varchar(255), zipcode varchar(255));
