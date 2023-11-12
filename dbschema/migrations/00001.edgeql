CREATE MIGRATION m17rjiuhkpy5vipyrlkmqoixl2wyg2tdkacm44bgv5twndhicci5jq
    ONTO initial
{
  CREATE EXTENSION pgcrypto VERSION '1.3';
  CREATE EXTENSION auth VERSION '1.0';
  CREATE TYPE default::User {
      CREATE REQUIRED LINK identity: ext::auth::Identity;
      CREATE REQUIRED PROPERTY name: std::str;
  };
  CREATE GLOBAL default::current_user := (std::assert_single((SELECT
      default::User {
          id,
          name
      }
  FILTER
      (.identity = GLOBAL ext::auth::ClientTokenIdentity)
  )));
};
