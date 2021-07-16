
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where lastName = 'Smithello' );

delete from lwa_sandbox.address
where lastName = 'Smithello';