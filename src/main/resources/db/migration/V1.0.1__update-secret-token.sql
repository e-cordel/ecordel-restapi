-- update the initial value that has invalid characters for new jjwt version
-- this used only for testing and to guarantee the server will be up and running locally without any issues in local dev
-- the production value is not and MUST to be managed by flyway migrations
update jwt_token set secret_key = 'TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdC4gU2VkIHRpbmNpZHVudCBhbnRlIGV0IG1hZ25hIHB1bHZpbmFyIGJpYmVuZHVtLiBEb25lYyBwdWx2aW5hciBkaWFtIGVnZXQgYXJjdSB2dWxwdXRhdGUgcG9zdWVyZS4gRHVpcyBzdXNjaXBpdCBpbiBmZWxpcyBldSB0ZW1wdXMuIERvbmVjIG5vbiBlZmZpY2l0dXIgb3JjaS4gU2VkIGFjIHNvbGxpY2l0dWRpbiBuaXNpLiBOYW0gbmliaCByaXN1cywgZmFjaWxpc2lzIHZpdGFlIGJpYmVuZHVtIHZpdGFlLCBlZmZpY2l0dXIgZXgu';