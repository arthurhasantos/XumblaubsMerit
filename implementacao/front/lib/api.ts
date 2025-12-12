const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"

/** * Trata a resposta HTTP de forma consistente, verificando status e convertendo o conteúdo. 
 * @param {Response} response - Resposta da requisição fetch 
 * @returns {Promise<any>} - Dados convertidos ou erro tratado 
 */
const handleResponse = async (response: Response): Promise<any> => {
  const isJson = response.headers.get("content-type")?.includes("application/json")

  const data = !isJson
      ? await response.text().catch(() => null)
      : await response.json().catch(() => null)


  if (!response.ok) {
    throw new Error(!isJson ? data : data?.message)
  } else {
    return data
  }
}

/**
 * Função global de request baseada em fetch para padronizar chamadas.
 * - Envia cookies automaticamente
 * - Aplica headers padrão
 *
 * @param {string} endpoint - Caminho da rota da API
 * @param {RequestInit} options - Opções da requisição
 * @returns {Promise<any>} - Dados convertidos via handleResponse()
 */
const request = async (endpoint: string, options: RequestInit): Promise<any> => {
  const response = await fetch(`${API_BASE_URL}/api${endpoint}`, {
    ...options,
    credentials: "include", 
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
  })

  return handleResponse(response)
}

const api = {
  post: (endpoint: string, data?: any) =>
    request(endpoint, 
    {
      method: "POST",
      body: JSON.stringify(data),
    }
  ),
  get: (endpoint: string) => 
    request(endpoint, 
    { 
      method: "GET" 
    }
  ),
  put: (endpoint: string, data?: any) =>
    request(endpoint, 
    {
      method: "PUT",
      body: JSON.stringify(data),
    }
  ),
  delete: (endpoint: string) => 
  request(endpoint, 
    { 
      method: "DELETE" 
    }
  ).then(() => true)
}

export default api
