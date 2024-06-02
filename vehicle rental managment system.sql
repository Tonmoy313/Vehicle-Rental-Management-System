create database vehicle_rental_mangment_system
use vehicle_rental_mangment_system
-->////////////////////////////////////////////////////////
-->sign in table
create table signin
(
userid int primary key identity(1,1) NOT NULL,
LoginName VARCHAR(40) NOT NULL,
Email VARCHAR (100) not null CHECK (Email like '%@gmail.com'),
PasswordHash BINARY(64) NOT NULL,
FirstName VARCHAR(40) NULL,
LastName VARCHAR(40) NULL,
)
/*drop table signin*/
select * from signin
insert into signin values('admin','123@gmail.com',CONVERT(binary, 'admin'),'Admin','Admin'),
						 ('Tonmoy','tonmoy@gmail.com',CONVERT(binary, 'tonmoy'),'Abdullah','Tonmoy'),
						 ('Sanjida','sanjida@gmail.com',CONVERT(binary, 'sanjida'),'Sanjida','Sheikh'),
						 ('Shejuti','shejuti@gmail.com',CONVERT(binary, 'shejuti'),'Nafisa','Tabassum')
insert into signin (FirstName,LastName,Email,LoginName,PasswordHash)values ('admin','admin','admin@gmail.com','admin',CONVERT(binary,'admin'))
SELECT LoginName FroM SIGNIN where PasswordHash=convert(binary,'admin')
SELECT CONVERt(varchar,PasswordHash) FroM SIGNIN where LoginName='admin' 
Select PasswordHash from signin where loginname='admin' 

create table Customer
(
CustomerId int primary key identity(1,1) NOT NULL,
Name VARCHAR(40) NOT NULL,
PhoneNo VARCHAR (11) NOT null check (ISNUMERIC(PhoneNo)=1 and LEN(PhoneNo)=11 ),
NID varchar(50)  NOT NULL,
Cstmr_Licence varchar(15),
DriverHire varchar(1) not null default 'Y',
customerPic varbinary(max) not null
)

/*drop table Customer*/
select * from Customer
insert into Customer values('Tonmoy','01554319011','tonuNID','tonuLicence','Y',(select * from openrowset(bulk 'C:\image\tonmoy.png',single_blob) as t1))

select CONVERT(varbinary(max),customerPic,1),NID from Customer where Name='Tonmoy'

select customerPic from Customer where Name='Tonmoy'

update Customer set Name='Sanja Ahmed Roshni', PhoneNo='01866201985',NID='roshniNid', Cstmr_Licence='roshniLic',DriverHire='N',customerPic= (select * from openrowset(bulk 'C:\image\tonmoy.png',single_blob) as t1)
where CustomerId=3 

-->////////////////////////////////////////////////////////////////////////////////////////////////////////
-->Vehicle Table
create table Vehicle
(
VehicleId int primary key identity(1,1) NOT NULL,
Category VARCHAR(40) not null,
Wheel int not null,
Vehicle_Model VARCHAR(40) NOT NULL,
Vehicle_EngNo varchar(40),
Vehicle_RegNo VARCHAR (40) NOT null,
Vehicle_Price decimal(9,2) not null, 
Availabilty varchar(1) default 'Y',
vehicle_Pic varbinary(max)
)
/*drop table Vehicle*/
select* from Vehicle
select VehicleId,vehicle_Pic from Vehicle where Vehicle_EngNo=?
select Category,Wheel,Vehicle_Model,Vehicle_EngNo,Vehicle_RegNo, Vehicle_Price ,Availabilty from Vehicle 
select Category, Wheel,Vehicle_Model, Vehicle_EngNo, Vehicle_RegNo,Vehicle_Price,Availabilty from Vehicle

Insert into Vehicle values('CAR',4,'PREMIO','1NZ-FE  3ZR-FAE I4','DHAKA METRO-GA 42-3833','9500','Y')
Insert into Vehicle values('CAR',4,'ALLION','2ZR-FE','DHAKA METRO-GHA 37-1223','10000','Y'),
						('CAR',4,'COROLLA','3ZR-FAE ','DHAKA METRO-KA 36-1024','5000','Y')
Insert into Vehicle values('MICRO-BUS',4,'NOAH','3ZR-FAE','DHAKA METRO-kha 39-3733','11000','Y'),
('BIKE',2,'R15','3ZR-FAE I4','DHAKA METRO-gha 39-3133','2000','Y')
-->////////////////////////////////////////////////////////////////////////////////////////////////////////////////
-->Payment
create table Payment
(
PaymentId int primary key identity(1,1) NOT NULL,
Netprice decimal(7,2),
AdvanceTk decimal(7,2),
Fine decimal(7,2),
Total AS (Netprice-AdvanceTk+Fine),
PaidStatus varchar(1) default 'N'
)
Insert into Payment(Netprice,AdvanceTk) values('20000','10000')
                        
/*drop table Payment*/
select* from Payment
select CarId,Car_RegNo,Car_brand,Car_Model, Availabilty,Car_Price from Cars where Availabilty='Y'
insert into Payment( AdvanceTk,DueTk) values (1500,3300.50-1500)
-->//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
-->Returncar table\
-->//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
-->Booked car table
create table BookedCar
(
BookedCarId int primary key identity(1,1) NOT NULL,
Booked_Date date NOT NULL,
Arrival_Date  date NOT null,
Returned_Date date DEFAULT NULL,
late int DEFAULT 0,
CustomerId int FOREIGN KEY REFERENCES Customer(CustomerId),
VehicleId  int FOREIGN KEY REFERENCES Vehicle (VehicleId ),
PaymentId int FOREIGN KEY REFERENCES Payment(PaymentId)
)
select cstmr.Name,cstmr.PhoneNo,cr.Vehicle_Model,cr.Vehicle_RegNo,cr.Vehicle_Price,
bkedcr.Booked_date,bkedcr.Arrival_date,
bkedcr.returned_date,bkedcr.late,
pay.Netprice,pay.AdvanceTk,pay.PaidStatus
from BookedCar as bkedcr
inner join Customer as cstmr on bkedcr.CustomerId=cstmr.CustomerId
inner join Vehicle as cr on bkedcr.VehicleId=cr.VehicleId
inner join Payment as pay on bkedcr.PaymentId=pay.PaymentId
where BookedCarId=?   
                
update payment 
set payment.PaidStatus='Y',payment.Total=?,payment.fine=?
from payment pay,BookedCar bkdcr
where bkdcr.PaymentId=pay.PaymentId
and bkdcr.BookedCarId=? 
                        
select cstmr.Name,cr.Vehicle_RegNo,rntcr.Booked_date,rntcr.Arrival_date,rntcr.returned_date,rntcr.late
from BookedCar as rntcr
inner join Customer as cstmr on rntcr.CustomerId=cstmr.CustomerId
inner join Vehicle as cr on rntcr.VehicleId=cr.VehicleId
order by rntcr.Arrival_Date
                    
select cstmr.Name,cr.Vehicle_RegNo,bkdcr.Booked_date,bkdcr.Arrival_date,bkdcr.Returned_Date,bkdcr.late 
from BookedCar bkdcr
inner join Customer cstmr on bkdcr.CustomerId=cstmr.CustomerId
inner join Vehicle cr on bkdcr.VehicleId=cr.VehicleId
inner join Payment pay on bkdcr.PaymentId=pay.PaymentId 
where bkdcr.Arrival_Date>CONVERT(date,getdate()) and pay.PaidStatus='Y'
order by bkdcr.Arrival_Date
                    
select bkdcr.BookedCarId,cstmr.PhoneNo,cstmr.Cstmr_Licence,cr.Availabilty
from BookedCar as bkdcr
inner join Customer as cstmr on bkdcr.CustomerId=cstmr.CustomerId
inner join Vehicle as cr on bkdcr.VehicleId=cr.VehicleId
where cstmr.Name=? and cr.Vehicle_RegNo=? and bkdcr.Booked_Date=? and bkdcr.Arrival_Date=?
                
UPDATE bkdcr
SET bkdcr.Returned_Date =?,bkdcr.late=?
FROM BookedCar bkdcr, Vehicle cr
WHERE bkdcr.VehicleId=cr.VehicleId
and BookedCarId=?

UPDATE cr
SET cr.Availabilty='Y'
FROM BookedCar bkdcr, Vehicle cr
WHERE bkdcr.VehicleId=cr.VehicleId
and BookedCarId=? 

Insert into Payment(Netprice,AdvanceTk) values('20000','10000')
insert into BookedCar(Booked_Date,Arrival_Date,CustomerId,VehicleId,PaymentId)
values ('2020/2/20','2020/2/20',3,1,IDENT_CURRENT( 'Payment'))
UPDATE cr SET cr.Availabilty='N' FROM BookedCar bkdcr, Vehicle cr
WHERE bkdcr.VehicleId =cr.VehicleId and BookedCarId=IDENT_CURRENT( 'BookedCar')

Truncate  Table Payment
/*drop table BookedCar*/
insert into Payment(AdvanceTk,DueTk) values ('100',
                        insert into BookedCar(Booked_Date,Arrival_Date,CustomerId,VehicleId ,PaymentId) 
                        values (?,?,?,?,IDENT_CURRENT( 'Payment'))
                        UPDATE cr
                        SET cr.Availabilty='N'
                        FROM BookedCar bkdcr, Vehicle cr
                        WHERE bkdcr.VehicleId =cr.VehicleId 
                        and BookedCarId=IDENT_CURRENT( 'BookedCar')

Truncate  Table Bookedcar
select* from BookedCar
select* from Vehicle
select* from Customer
select* from Payment

insert into Payment(AdvanceTk,DueTk) select ?,cr.Vehicle_Price-? FROM Vehicle cr where cr.VehicleId =?
                        
                        insert into BookedCar(Booked_Date,Arrival_Date,CustomerId,VehicleId ,PaymentId) 
                        values (?,?,?,?,IDENT_CURRENT( 'Payment'))
                        UPDATE cr
                        SET cr.Availabilty='N'
                        FROM BookedCar bkdcr, Vehicle cr
                        WHERE bkdcr.VehicleId =cr.VehicleId 
                        and BookedCarId=IDENT_CURRENT( 'BookedCar')

insert into Payment(AdvanceTk,DueTk) 
select '700',cr.Car_Price-'700'
FROM Cars cr 
where cr.CarId=1

insert into BookedCar(Booked_Date,Arrival_Date,CustomerId,CarId,PaymentId) values ('2022/3/2','2022/3/28',1,1,IDENT_CURRENT( 'Payment'))

UPDATE cr
SET cr.Availabilty='N'
FROM BookedCar bkdcr, Cars cr
WHERE bkdcr.CarId=cr.CarId
and BookedCarId=IDENT_CURRENT( 'BookedCar')

-->insert into BookedCar(Booked_Date,Arrival_Date,CustomerId,CarId,PaymentId) values ('2022/1/27','2022/2/28',1,1,IDENT_CURRENT( 'Payment'))
