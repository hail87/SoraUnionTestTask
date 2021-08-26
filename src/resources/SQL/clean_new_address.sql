delete from lwa_sandbox.accountAddress
where addressID = (SELECT addressID FROM lwa_sandbox.address where lastName = 'Woznik');
--order by accountAddressID desc limit 1;

delete from lwa_sandbox.address
where lastName = 'Woznik';
--order by addressID desc limit 1;
