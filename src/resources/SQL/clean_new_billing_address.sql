
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Wp5T66EYgSIiedunoJH1vw==' and lastName = 'PwcFBLqjwpzAFbiKW1geBw=='); -- where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Andrew' and lastName = 'Jonson');

delete from lwa_sandbox.address
where firstName = 'Wp5T66EYgSIiedunoJH1vw==' and lastName = 'PwcFBLqjwpzAFbiKW1geBw=='; --where firstName = 'Andrew' and lastName = 'Jonson'