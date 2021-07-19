
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Samuel' and lastName = 'Montana');

delete from lwa_sandbox.address
where firstName = 'Samuel' and lastName = 'Montana';