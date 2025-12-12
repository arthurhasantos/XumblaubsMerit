"use client"

import { createContext, useContext, ReactNode } from "react"
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import api from "@/services/api"

interface User {
  id: number
  name: string
  email: string
  roles: string[]
}

interface AuthContextType {
  user: User | null
  loading: boolean
  login: (email: string, password: string) => Promise<boolean>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextType>(undefined)

/**
 * Hook de acesso ao contexto de autenticação.
 * Garante que o hook só é usado dentro do provider.
 *
 * @returns {AuthContextType} Objeto contendo usuário, loading e ações
 */
export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error("useAuth must be used within AuthProvider")
  return ctx
}

/**
 * Provider responsável por gerenciar:
 * - Estado de autenticação via React Query
 * - Login/logout usando cookies httpOnly
 * - Cache do usuário (auth/me)
 *
 * @param {{ children: ReactNode }} props - Elementos a serem renderizados dentro do contexto
 * @returns {JSX.Element} Provider com valores de autenticação
 */
export const AuthProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const queryClient = useQueryClient()

  /**
   * Busca inicial do usuário autenticado.
   * O backend identifica o usuário via cookie httpOnly.
   *
   * @returns {User} Usuário autenticado ou null
   */
  const { data: user, isLoading: loading } = useQuery<User>({
    queryKey: ["auth"],
    queryFn: async () => {
      return await api.get("/auth/me")
    },
    retry: false// evita loops em caso de 401
  })

  /**
   * Mutation responsável por autenticar o usuário.
   * Envia credenciais, backend devolve cookie httpOnly.
   *
   * @param {{ email: string, password: string }} params - Credenciais do usuário
   * @returns {Promise<void>}
   */
  const loginMutation = useMutation({
    mutationFn: async (params: { email: string; password: string }) => {
      await api.post("/auth/login", {
        email: params.email,
        senha: params.password,
      })
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["auth"] })
    }
  })

  /**
   * Mutation responsável por remover sessão.
   * Backend apaga o cookie httpOnly.
   *
   * @returns {Promise<void>}
   */
  const logoutMutation = useMutation({
    mutationFn: async () => {
      await api.post("/auth/logout")
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["auth"] })
    }
  })

  const value: AuthContextType = {
    user,
    loading,
     /**
     * Efetua login usando cookies httpOnly.
     *
     * @param {string} email
     * @param {string} password
     * @returns {Promise<boolean>} true se autenticado, false se falhou
     */
    login: async (email: string, password: string): Promise<boolean> => {
        try {
          await loginMutation.mutateAsync({ email, password })
          return true
        } catch {
          return false
        }
    },
    /**
     * Efetua logout limpando o cookie no backend.
     *
     * @returns {Promise<void>}
     */
    logout: async (): Promise<void> => {
      await logoutMutation.mutateAsync()
    }
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}
