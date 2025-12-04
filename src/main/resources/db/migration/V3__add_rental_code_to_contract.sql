ALTER TABLE contract 
ADD COLUMN rental_code VARCHAR(10),
ADD COLUMN renter_user_id BIGINT,
ADD FOREIGN KEY (renter_user_id) REFERENCES user(id);

CREATE INDEX idx_contract_rental_code ON contract(rental_code);
CREATE INDEX idx_contract_renter_user_id ON contract(renter_user_id);

