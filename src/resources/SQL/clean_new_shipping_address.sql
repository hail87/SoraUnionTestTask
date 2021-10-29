
delete from lwa_sandbox.accountAddress
where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'cs95lN2+SZl8sa9RlILLTw==' and lastName = '1sVI9jIf1EMZrunyoW/vSA=='); --where addressID = ( SELECT addressID FROM lwa_sandbox.address where firstName = 'Samuel' and lastName = 'Montana');

delete from lwa_sandbox.address
where firstName = 'cs95lN2+SZl8sa9RlILLTw==' and lastName = '1sVI9jIf1EMZrunyoW/vSA=='; --where firstName = 'Samuel' and lastName = 'Montana';