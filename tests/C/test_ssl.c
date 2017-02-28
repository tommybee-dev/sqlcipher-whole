#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
  unsigned char* key = (unsigned char*) "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b";
  unsigned char* data = (unsigned char*) "4869205468657265";
  unsigned char* expected = (unsigned char*) "b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7";
  unsigned char* result;
  HMAC_CTX* ctx;
  ENGINE* e;

  //ENGINE_load_builtin_engines();
  //ENGINE_register_all_complete();
  //ENGINE_set_default_digests(e);

  //HMAC_CTX_init(ctx);
  HMAC_Init_ex(ctx, key, 40, EVP_sha256(), e);
  result = HMAC(NULL, NULL, 40, data, 16, NULL, NULL);
  HMAC_CTX_free(ctx);

  //ENGINE_finish(e);
  //ENGINE_free(e);

  if (strcmp((char*) result, (char*) expected) == 0) {
    printf("Test ok\n");
  } else {
    printf("Got %s instead of %s\n", result, expected);
  }
}