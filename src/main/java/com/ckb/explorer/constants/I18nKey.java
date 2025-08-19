package com.ckb.explorer.constants;

/**
 * internationalization message key
 */
public final class I18nKey {

  private I18nKey(){}

  // Block related errors
  public static final int BLOCK_QUERY_KEY_INVALID_CODE = 1003;
  public static final String BLOCK_QUERY_KEY_INVALID_MESSAGE = "uri_parameters_should_be_a_block_hash_or_a_block_height";

  public static final int BLOCK_NOT_FOUND_CODE = 1004;
  public static final String BLOCK_NOT_FOUND_MESSAGE = "block_not_found";

  public static final int BLOCK_HASH_INVALID_CODE = 1005;
  public static final String BLOCK_HASH_INVALID_MESSAGE = "block_hash_invalid";

  public static final int BLOCK_TRANSACTIONS_NOT_FOUND_CODE = 1006;
  public static final String BLOCK_TRANSACTIONS_NOT_FOUND_MESSAGE = "block_transactions_not_found";

  // Pagination related errors
  public static final int PAGE_PARAM_ERROR_CODE = 1007;
  public static final String PAGE_PARAM_ERROR_MESSAGE = "page_param_error";

  public static final int PAGE_SIZE_PARAM_ERROR_CODE = 1008;
  public static final String PAGE_SIZE_PARAM_ERROR_MESSAGE = "page_size_param_error";

  // Address related errors
  public static final int ADDRESS_HASH_INVALID_CODE = 1009;
  public static final String ADDRESS_HASH_INVALID_MESSAGE = "address_hash_invalid";

  public static final int ADDRESS_NOT_FOUND_CODE = 1010;
  public static final String ADDRESS_NOT_FOUND_MESSAGE = "address_not_found";

  public static final int ADDRESS_NOT_MATCH_ENVIRONMENT_CODE = 1023;
  public static final String ADDRESS_NOT_MATCH_ENVIRONMENT_MESSAGE = "address_not_match_environment";

  // Transaction related errors
  public static final int CKB_TRANSACTION_TX_HASH_INVALID_CODE = 1011;
  public static final String CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE = "ckb_transaction_tx_hash_invalid";

  public static final int CKB_TRANSACTION_NOT_FOUND_CODE = 1012;
  public static final String CKB_TRANSACTION_NOT_FOUND_MESSAGE = "ckb_transaction_not_found";

  // Cell related errors
  public static final int CELL_INPUT_ID_INVALID_CODE = 1013;
  public static final String CELL_INPUT_ID_INVALID_MESSAGE = "cell_input_id_invalid";

  public static final int CELL_INPUT_NOT_FOUND_CODE = 1014;
  public static final String CELL_INPUT_NOT_FOUND_MESSAGE = "cell_input_not_found";

  public static final int CELL_OUTPUT_ID_INVALID_CODE = 1015;
  public static final String CELL_OUTPUT_ID_INVALID_MESSAGE = "cell_output_id_invalid";

  public static final int CELL_OUTPUT_NOT_FOUND_CODE = 1016;
  public static final String CELL_OUTPUT_NOT_FOUND_MESSAGE = "cell_output_not_found";

  public static final int CELL_OUTPUT_DATA_SIZE_EXCEEDS_LIMIT_CODE = 1017;
  public static final String CELL_OUTPUT_DATA_SIZE_EXCEEDS_LIMIT_MESSAGE = "cell_output_data_size_exceeds_limit";

  // Script related errors
  public static final int SCRIPT_NOT_FOUND_CODE = 1018;
  public static final String SCRIPT_NOT_FOUND_MESSAGE = "script_not_found";

  public static final int SCRIPT_CODE_HASH_PARAMS_INVALID_CODE = 1019;
  public static final String SCRIPT_CODE_HASH_PARAMS_INVALID_MESSAGE = "script_code_hash_params_invalid";

  public static final String SORT_ERROR_MESSAGE = "sort_error";

  public static final int SCRIPT_HASH_TYPE_PARAMS_INVALID_CODE = 1020;
  public static final String SCRIPT_HASH_TYPE_PARAMS_INVALID_MESSAGE = "script_hash_type_params_invalid";

  // Indicator related errors
  public static final int INDICATOR_NAME_INVALID_CODE = 1021;
  public static final String INDICATOR_NAME_INVALID_MESSAGE = "indicator_name_invalid";

  // NetInfo related errors
  public static final int NET_INFO_NAME_INVALID_CODE = 1022;
  public static final String NET_INFO_NAME_INVALID_MESSAGE = "net_info_name_invalid";

  // Contract related errors
  public static final int CONTRACT_NOT_FOUND_CODE = 1024;
  public static final String CONTRACT_NOT_FOUND_MESSAGE = "contract_not_found";

  // Statistic related errors
  public static final int STATISTIC_INFO_NAME_INVALID_CODE = 1025;
  public static final String STATISTIC_INFO_NAME_INVALID_MESSAGE = "statistic_info_name_invalid";

  // Suggest query related errors
  public static final int SUGGEST_QUERY_KEY_INVALID_CODE = 1026;
  public static final String SUGGEST_QUERY_KEY_INVALID_MESSAGE = "suggest_query_key_invalid";

  public static final int SUGGEST_QUERY_RESULT_NOT_FOUND_CODE = 1027;
  public static final String SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE = "suggest_query_result_not_found";

  // TypeHash related errors
  public static final int TYPE_HASH_INVALID_CODE = 1028;
  public static final String TYPE_HASH_INVALID_MESSAGE = "type_hash_invalid";

  // UDT related errors
  public static final int UDT_INFO_INVALID_CODE = 1029;
  public static final String UDT_INFO_INVALID_MESSAGE = "udt_info_invalid";

  public static final int UDT_NOT_FOUND_CODE = 1030;
  public static final String UDT_NOT_FOUND_MESSAGE = "udt_not_found";

  public static final int UDT_VERIFICATION_INVALID_CODE = 1031;
  public static final String UDT_VERIFICATION_INVALID_MESSAGE = "udt_verification_invalid";

  public static final int UDT_VERIFICATION_NOT_FOUND_CODE = 1032;
  public static final String UDT_VERIFICATION_NOT_FOUND_MESSAGE = "udt_verification_not_found";

  public static final int UDT_NO_CONTACT_EMAIL_CODE = 1033;
  public static final String UDT_NO_CONTACT_EMAIL_MESSAGE = "udt_no_contact_email";

  // Token related errors
  public static final int TOKEN_EXPIRED_CODE = 1034;
  public static final String TOKEN_EXPIRED_MESSAGE = "token_expired";

  public static final int TOKEN_NOT_EXIST_CODE = 1035;
  public static final String TOKEN_NOT_EXIST_MESSAGE = "token_not_exist";

  public static final int TOKEN_SENT_TOO_FREQUENTLY_CODE = 1036;
  public static final String TOKEN_SENT_TOO_FREQUENTLY_MESSAGE = "token_sent_too_frequently";

  public static final int TOKEN_NOT_MATCH_CODE = 1037;
  public static final String TOKEN_NOT_MATCH_MESSAGE = "token_not_match";

  // Content type related errors
  public static final int INVALID_CONTENT_TYPE_CODE = 1038;
  public static final String INVALID_CONTENT_TYPE_MESSAGE = "invalid_content_type";

  public static final int INVALID_ACCEPT_CODE = 1039;
  public static final String INVALID_ACCEPT_MESSAGE = "invalid_accept";
}
