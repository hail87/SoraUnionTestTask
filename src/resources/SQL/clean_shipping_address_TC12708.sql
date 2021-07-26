
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Erika' and lastName = 'Spoon');

delete from lwa_sandbox.address
where firstName = 'Erika' and lastName = 'Spoon';