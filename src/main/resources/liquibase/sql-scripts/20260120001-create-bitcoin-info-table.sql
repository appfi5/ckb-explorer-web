CREATE TABLE IF NOT EXISTS bitcoin_transactions (
                                                    id BIGSERIAL PRIMARY KEY,
                                                    txid bytea,
                                                    tx_hash bytea,
                                                    "time" bigint,
                                                    block_hash bytea,
                                                    block_height bigint
);


CREATE UNIQUE INDEX IF NOT EXISTS index_bitcoin_transactions_on_txid ON bitcoin_transactions USING btree (txid);



CREATE TABLE IF NOT EXISTS bitcoin_vouts (
                                             id BIGSERIAL PRIMARY KEY,
                                             bitcoin_transaction_id bigint,
                                             bitcoin_address_id bigint,
                                             data bytea,
                                             index integer,
                                             asm text,
                                             op_return boolean DEFAULT false,
                                             ckb_transaction_id bigint,
                                             cell_output_id bigint,
                                             address_id bigint,
                                             status integer DEFAULT 0,
                                             consumed_by_id bigint
);

CREATE INDEX IF NOT EXISTS index_bitcoin_vouts_on_bitcoin_address_id ON  bitcoin_vouts USING btree (bitcoin_address_id);
CREATE INDEX IF NOT EXISTS index_bitcoin_vouts_on_ckb_transaction_id ON  bitcoin_vouts USING btree (ckb_transaction_id);
CREATE INDEX IF NOT EXISTS index_bitcoin_vouts_on_consumed_by_id ON  bitcoin_vouts USING btree (consumed_by_id);


CREATE TABLE IF NOT EXISTS bitcoin_vins (
                                            id BIGSERIAL PRIMARY KEY,
                                            previous_bitcoin_vout_id bigint,
                                            ckb_transaction_id bigint,
                                            cell_input_id bigint
);



CREATE INDEX IF NOT EXISTS index_bitcoin_vins_on_ckb_transaction_id ON bitcoin_vins USING btree (ckb_transaction_id);

CREATE UNIQUE INDEX IF NOT EXISTS index_bitcoin_vins_on_ckb_transaction_id_and_cell_input_id ON  bitcoin_vins USING btree (ckb_transaction_id, cell_input_id);


CREATE TABLE IF NOT EXISTS bitcoin_transfers (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 bitcoin_transaction_id bigint,
                                                 ckb_transaction_id bigint,
                                                 cell_output_id bigint,
                                                 lock_type integer DEFAULT 0
);


CREATE INDEX IF NOT EXISTS index_bitcoin_transfers_on_bitcoin_transaction_id ON bitcoin_transfers USING btree (bitcoin_transaction_id);

CREATE UNIQUE INDEX IF NOT EXISTS index_bitcoin_transfers_on_cell_output_id ON bitcoin_transfers USING btree (cell_output_id);

CREATE INDEX IF NOT EXISTS index_bitcoin_transfers_on_ckb_transaction_id ON bitcoin_transfers USING btree (ckb_transaction_id);


CREATE TABLE IF NOT EXISTS bitcoin_addresses (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 address_hash VARCHAR NOT NULL
);



CREATE TABLE IF NOT EXISTS btc_account_books (
                                                 id BIGSERIAL PRIMARY KEY,
                                                 ckb_transaction_id bigint,
                                                 bitcoin_address_id bigint
);

CREATE INDEX IF NOT EXISTS index_btc_account_books_on_ckb_transaction_id ON btc_account_books USING btree (ckb_transaction_id);
CREATE INDEX IF NOT EXISTS index_btc_account_books_on_bitcoin_address_id ON btc_account_books USING btree (bitcoin_address_id);
CREATE UNIQUE INDEX IF NOT EXISTS index_btc_account_books_on_ckb_transaction_id_and_bitcoin_addre ON  btc_account_books USING btree (ckb_transaction_id, bitcoin_address_id);

CREATE TABLE IF NOT EXISTS bitcoin_annotations (
                                                   id BIGSERIAL PRIMARY KEY,
                                                   ckb_transaction_id bigint,
                                                   leap_direction integer,
                                                   transfer_step integer,
                                                   tags character varying[] DEFAULT '{}'::character varying[]
);


CREATE UNIQUE INDEX IF NOT EXISTS index_bitcoin_annotations_on_ckb_transaction_id ON bitcoin_annotations USING btree (ckb_transaction_id);
