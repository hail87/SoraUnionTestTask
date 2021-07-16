
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Andrew' and lastName = 'Jonson');

delete from lwa_sandbox.address
where firstName = 'Andrew' and lastName = 'Jonson';