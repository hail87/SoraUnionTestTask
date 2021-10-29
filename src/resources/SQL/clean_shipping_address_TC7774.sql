
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Wp5T66EYgSIiedunoJH1vw==' and lastName = 'OYZN5o1tJoARyZZy+e5AnQ==');

delete from lwa_sandbox.address
where firstName = 'Wp5T66EYgSIiedunoJH1vw==' and lastName = 'OYZN5o1tJoARyZZy+e5AnQ==';

--delete from lwa_sandbox.accountAddress
--where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Andrew' and lastName = 'Paterson');
--
--delete from lwa_sandbox.address
--where firstName = 'Andrew' and lastName = 'Paterson';