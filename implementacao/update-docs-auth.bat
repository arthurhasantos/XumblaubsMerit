@echo off
echo Atualizando documentacao com headers de autorizacao...

powershell -Command "(Get-Content 'API_CRUD_DOCUMENTATION.md') -replace '```http\s*GET /api/', '```http\nGET /api/\nAuthorization: Bearer eyJhbGciOiJIUzUxMiJ9...' | Set-Content 'API_CRUD_DOCUMENTATION.md'"

powershell -Command "(Get-Content 'API_CRUD_DOCUMENTATION.md') -replace '```http\s*POST /api/', '```http\nPOST /api/\nAuthorization: Bearer eyJhbGciOiJIUzUxMiJ9...' | Set-Content 'API_CRUD_DOCUMENTATION.md'"

powershell -Command "(Get-Content 'API_CRUD_DOCUMENTATION.md') -replace '```http\s*PUT /api/', '```http\nPUT /api/\nAuthorization: Bearer eyJhbGciOiJIUzUxMiJ9...' | Set-Content 'API_CRUD_DOCUMENTATION.md'"

powershell -Command "(Get-Content 'API_CRUD_DOCUMENTATION.md') -replace '```http\s*DELETE /api/', '```http\nDELETE /api/\nAuthorization: Bearer eyJhbGciOiJIUzUxMiJ9...' | Set-Content 'API_CRUD_DOCUMENTATION.md'"

echo Documentacao atualizada!
pause
