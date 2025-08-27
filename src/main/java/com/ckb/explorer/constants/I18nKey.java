package com.ckb.explorer.constants;

/**
 * internationalization message key
 */
public final class I18nKey {

  private I18nKey(){}

  // Content type errors
  public static final int INVALID_CONTENT_TYPE_CODE = 1001;
  public static final String INVALID_CONTENT_TYPE_MESSAGE = "content_type_must_be_application_vnd_api_json";

  public static final int INVALID_ACCEPT_CODE = 1002;
  public static final String INVALID_ACCEPT_MESSAGE = "accept_must_be_application_vnd_api_json";

  // Block related errors
  public static final int BLOCK_QUERY_KEY_INVALID_CODE = 1003;
  public static final String BLOCK_QUERY_KEY_INVALID_MESSAGE = "uri_parameters_should_be_a_block_hash_or_a_block_height";

  public static final int BLOCK_NOT_FOUND_CODE = 1004;
  public static final String BLOCK_NOT_FOUND_MESSAGE = "block_not_found";

  public static final int BLOCK_HASH_INVALID_CODE = 1011;
  public static final String BLOCK_HASH_INVALID_MESSAGE = "uri_parameters_should_be_a_block_hash";

  public static final int BLOCK_TRANSACTIONS_NOT_FOUND_CODE = 1012;
  public static final String BLOCK_TRANSACTIONS_NOT_FOUND_MESSAGE = "no_transaction_records_found_by_given_address_hash";

  // Transaction related errors
  public static final int CKB_TRANSACTION_TX_HASH_INVALID_CODE = 1005;
  public static final String CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE = "uri_parameters_should_be_a_transaction_hash";

  public static final int CKB_TRANSACTION_NOT_FOUND_CODE = 1006;
  public static final String CKB_TRANSACTION_NOT_FOUND_MESSAGE = "no_transaction_records_found_by_given_transaction_hash";
  //
  // Page parameter errors
  public static final int PAGE_PARAM_ERROR_CODE = 1007;
  public static final String PAGE_PARAM_ERROR_MESSAGE = "params_page_should_be_an_integer";

  public static final int PAGE_SIZE_PARAM_ERROR_CODE = 1008;
  public static final String PAGE_SIZE_PARAM_ERROR_MESSAGE = "params_page_size_should_be_an_integer";

  // Address errors
  public static final int ADDRESS_HASH_INVALID_CODE = 1009;
  public static final String ADDRESS_HASH_INVALID_MESSAGE = "uri_parameters_should_be_a_address_hash";

  public static final int ADDRESS_NOT_FOUND_CODE = 1010;
  public static final String ADDRESS_NOT_FOUND_MESSAGE = "no_address_found_by_given_address_hash_or_lock_hash";

  public static final int ADDRESS_NOT_MATCH_ENVIRONMENT_CODE = 1023;
  public static final String ADDRESS_NOT_MATCH_ENVIRONMENT_MESSAGE = "this_address_is_not_the_environment_address";

  // Cell input errors
  public static final int CELL_INPUT_ID_INVALID_CODE = 1013;
  public static final String CELL_INPUT_ID_INVALID_MESSAGE = "uri_parameters_should_be_a_integer";

  public static final int CELL_INPUT_NOT_FOUND_CODE = 1014;
  public static final String CELL_INPUT_NOT_FOUND_MESSAGE = "no_cell_input_records_found_by_given_id";

  // Cell output errors
  public static final int CELL_OUTPUT_ID_INVALID_CODE = 1015;
  public static final String CELL_OUTPUT_ID_INVALID_MESSAGE = "uri_parameters_should_be_a_integer";

  public static final int CELL_OUTPUT_NOT_FOUND_CODE = 1016;
  public static final String CELL_OUTPUT_NOT_FOUND_MESSAGE = "no_cell_output_records_found_by_given_id";

  public static final int CELL_OUTPUT_DATA_SIZE_EXCEEDS_LIMIT_CODE = 1022;
  public static final String CELL_OUTPUT_DATA_SIZE_EXCEEDS_LIMIT_MESSAGE = "cell_output_data_size_exceeds_limit";

  // Query errors
  public static final int SUGGEST_QUERY_KEY_INVALID_CODE = 1017;
  public static final String SUGGEST_QUERY_KEY_INVALID_MESSAGE = "query_parameter_should_be_a_block_height_block_hash_tx_hash_or_address_hash";

  public static final int SUGGEST_QUERY_RESULT_NOT_FOUND_CODE = 1018;
  public static final String SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE ="no_records_found_by_given_query_key";

  // Statistic errors
  public static final int STATISTIC_INFO_NAME_INVALID_CODE = 1019;
  public static final String STATISTIC_INFO_NAME_INVALID_MESSAGE = "given_statistic_info_name_is_invalid";

  public static final int NET_INFO_NAME_INVALID_CODE = 1020;
  public static final String NET_INFO_NAME_INVALID_MESSAGE = "given_net_info_name_is_invalid";

  // Contract errors
  public static final int CONTRACT_NOT_FOUND_CODE = 1021;
  public static final String CONTRACT_NOT_FOUND_MESSAGE = "no_contract_records_found_by_given_contract_name";

  public static final int INDICATOR_NAME_INVALID_CODE = 1024;
  public static final String INDICATOR_NAME_INVALID_MESSAGE = "given_indicator_name_is_invalid";

  // Type hash errors
  public static final int TYPE_HASH_INVALID_CODE = 1025;
  public static final String TYPE_HASH_INVALID_MESSAGE = "uri_parameters_should_be_a_type_hash";

  // UDT errors
  public static final int UDT_NOT_FOUND_CODE = 1026;
  public static final String UDT_NOT_FOUND_MESSAGE = "no_udt_records_found_by_given_type_hash";

  public static final int UDT_INFO_INVALID_CODE = 1030;
  public static final String UDT_INFO_INVALID_MESSAGE = "udt_info_parameters_invalid";

  public static final int UDT_VERIFICATION_INVALID_CODE = 1031;
  public static final String UDT_VERIFICATION_INVALID_MESSAGE = "udt_verification_invalid";

  public static final int UDT_VERIFICATION_NOT_FOUND_CODE = 1032;
  public static final String UDT_VERIFICATION_NOT_FOUND_MESSAGE = "no_udt_verification_records_found_by_given_type_hash";

  public static final int UDT_NO_CONTACT_EMAIL_CODE = 1033;
  public static final String UDT_NO_CONTACT_EMAIL_MESSAGE = "udt_has_no_contact_email";

  // Token errors
  public static final int TOKEN_EXPIRED_CODE = 1034;
  public static final String TOKEN_EXPIRED_MESSAGE = "token_has_expired";

  public static final int TOKEN_NOT_MATCH_CODE = 1035;
  public static final String TOKEN_NOT_MATCH_MESSAGE = "token_is_not_matched";

  public static final int TOKEN_SENT_TOO_FREQUENTLY_CODE = 1036;
  public static final String TOKEN_SENT_TOO_FREQUENTLY_MESSAGE = "token_sent_too_frequently";

  public static final int TOKEN_NOT_EXIST_CODE = 1037;
  public static final String TOKEN_NOT_EXIST_MESSAGE = "token_is_required_when_you_update_udt_info";

  // Script errors
  public static final int SCRIPT_CODE_HASH_PARAMS_INVALID_CODE = 1027;
  public static final String SCRIPT_CODE_HASH_PARAMS_INVALID_MESSAGE = "code_hash_should_be_start_with_0x";

  public static final int SCRIPT_HASH_TYPE_PARAMS_INVALID_CODE = 1028;
  public static final String SCRIPT_HASH_TYPE_PARAMS_INVALID_MESSAGE = "hash_type_should_be_type";

  public static final int SCRIPT_NOT_FOUND_CODE = 1029;
  public static final String SCRIPT_NOT_FOUND_MESSAGE = "script_not_found";

  // Common errors
  public static final String SORT_ERROR_MESSAGE = "sort_error";
  public static final String URI_PARAMETERS_INVALID_MESSAGE = "uri_parameters_is_invalid";
  public static final String NOT_ACCEPTABLE_MESSAGE = "not_acceptable";
  public static final String UNSUPPORTED_MEDIA_TYPE_MESSAGE = "unsupported_media_type";
  public static final String QUERY_PARAMETER_INVALID_MESSAGE = "query_parameter_is_invalid";

  // Portfolio and user related errors
  public static final int PARAMS_INVALID_CODE = 2000;
  public static final String PARAMS_INVALID_MESSAGE = "params_are_invalid";

  public static final int TOKEN_COLLECTION_NOT_FOUND_CODE = 2001;
  public static final String TOKEN_COLLECTION_NOT_FOUND_MESSAGE = "no_token_collection_found_by_given_script_hash_or_id";

  public static final int ADDRESS_NOT_MATCH_ENVIRONMENT_CODE_V2 = 2022;
  public static final String ADDRESS_NOT_MATCH_ENVIRONMENT_MESSAGE_V2 = "this_address_is_not_the_environment_address_v2";

  public static final int INVALID_PORTFOLIO_MESSAGE_CODE = 2003;
  public static final String INVALID_PORTFOLIO_MESSAGE_MESSAGE = "portfolio_message_is_invalid";

  public static final int INVALID_PORTFOLIO_SIGNATURE_CODE = 2004;
  public static final String INVALID_PORTFOLIO_SIGNATURE_MESSAGE = "portfolio_signature_is_invalid";

  public static final int USER_NOT_EXIST_CODE = 2005;
  public static final String USER_NOT_EXIST_MESSAGE = "user_not_exist";

  public static final int DECODE_JWT_FAILED_CODE = 2006;
  public static final String DECODE_JWT_FAILED_MESSAGE = "decode_jwt_failed";

  public static final int PORTFOLIO_LATEST_DISCREPANCY_CODE = 2007;
  public static final String PORTFOLIO_LATEST_DISCREPANCY_MESSAGE = "portfolio_has_not_synchronized_the_latest_addresses";

  public static final int SYNC_PORTFOLIO_ADDRESSES_CODE = 2008;
  public static final String SYNC_PORTFOLIO_ADDRESSES_MESSAGE = "sync_portfolio_addresses_failed";

  public static final int ADDRESS_NOT_FOUND_CODE_V2 = 2009;
  public static final String ADDRESS_NOT_FOUND_MESSAGE_V2 = "no_address_found_by_given_address_hash_or_lock_hash_v2";

  // Fiber related errors
  public static final int FIBER_PEER_PARAMS_INVALID_CODE = 2010;
  public static final String FIBER_PEER_PARAMS_INVALID_MESSAGE = "fiber_peer_params_invalid";

  public static final int FIBER_PEER_NOT_FOUND_CODE = 2011;
  public static final String FIBER_PEER_NOT_FOUND_MESSAGE = "no_peer_found_by_given_peer_id";

  public static final int FIBER_CHANNEL_NOT_FOUND_CODE = 2012;
  public static final String FIBER_CHANNEL_NOT_FOUND_MESSAGE = "no_channel_found_by_given_channel_id";

  public static final int FIBER_GRAPH_NODE_NOT_FOUND_CODE = 2013;
  public static final String FIBER_GRAPH_NODE_NOT_FOUND_MESSAGE = "no_graph_node_found_by_given_node_id";

  public static final int BLOCK_CHAIN_INFO_NOT_FOUND_CODE = 2014;
  public static final String BLOCK_CHAIN_INFO_NOT_FOUND_MESSAGE = "blockchain_info_not_found";
}
