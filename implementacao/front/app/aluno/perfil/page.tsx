"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import toast from "react-hot-toast";

const PerfilAlunoPage = () => {
  const { user, token, updateUser } = useAuth();
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [alunoData, setAlunoData] = useState({
    nome: "",
    email: "",
    cpf: "",
    rg: "",
    endereco: "",
    curso: "",
    fotoPerfil: "",
    instituicaoId: null,
    saldoMoedas: 0
  });

  // Função para converter links do Imgur para formato direto
  const getDirectImageUrl = (url) => {
    if (!url) return null;
    
    if (url.includes('i.imgur.com')) {
      return url;
    }
    
    if (url.includes('imgur.com/a/') || url.includes('imgur.com/gallery/')) {
      const match = url.match(/imgur\.com\/(?:a|gallery)\/([a-zA-Z0-9]+)/);
      if (match && match[1]) {
        return `https://i.imgur.com/${match[1]}.jpg`;
      }
    }
    
    if (url.includes('imgur.com/') && !url.includes('i.imgur.com')) {
      const match = url.match(/imgur\.com\/([a-zA-Z0-9]+)/);
      if (match && match[1]) {
        return `https://i.imgur.com/${match[1]}.jpg`;
      }
    }
    
    return url;
  };

  useEffect(() => {
    if (user && user.roles.includes('ALUNO')) {
      fetchAlunoData();
    } else if (user && !user.roles.includes('ALUNO')) {
      toast.error('Acesso negado. Apenas alunos podem editar o perfil.');
      router.push('/');
    }
  }, [user, token, router]);

  const fetchAlunoData = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/alunos/me', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        setAlunoData({
          nome: data.nome || "",
          email: data.email || "",
          cpf: data.cpf || "",
          rg: data.rg || "",
          endereco: data.endereco || "",
          curso: data.curso || "",
          fotoPerfil: data.fotoPerfil || "",
          instituicaoId: data.instituicaoId || null,
          saldoMoedas: data.saldoMoedas || 0
        });
      } else {
        console.error('Erro ao buscar dados do aluno:', response.statusText);
        toast.error('Erro ao carregar dados do perfil');
      }
    } catch (error) {
      console.error('Erro ao buscar dados do aluno:', error);
      toast.error('Erro ao carregar dados do perfil');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    try {
      const response = await fetch('http://localhost:8080/api/alunos/me', {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          nome: alunoData.nome,
          email: alunoData.email,
          cpf: alunoData.cpf,
          rg: alunoData.rg,
          endereco: alunoData.endereco,
          curso: alunoData.curso,
          fotoPerfil: alunoData.fotoPerfil,
          instituicaoId: alunoData.instituicaoId,
          saldoMoedas: alunoData.saldoMoedas
        })
      });

      if (response.ok) {
        const updatedData = await response.json();
        setAlunoData(updatedData);
        
        // Atualizar o contexto de autenticação com os novos dados
        if (updateUser) {
          updateUser({
            ...user,
            name: updatedData.nome,
            fotoPerfil: updatedData.fotoPerfil
          });
        }
        
        toast.success('Perfil atualizado com sucesso!');
      } else {
        const errorData = await response.json();
        toast.error(errorData.message || 'Erro ao atualizar perfil');
      }
    } catch (error) {
      console.error('Erro ao atualizar perfil:', error);
      toast.error('Erro ao atualizar perfil');
    } finally {
      setSaving(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setAlunoData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 pt-24">
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-primary"></div>
        </div>
      </div>
    );
  }

  return (
    <ProtectedRoute>
      <div className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-4xl mx-auto">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              Meu Perfil
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Edite suas informações pessoais e foto de perfil
            </p>
          </div>

          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6">
            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Foto de Perfil */}
              <div className="flex flex-col items-center mb-6">
                <div className="relative mb-4">
                  {alunoData.fotoPerfil ? (
                    <img
                      src={getDirectImageUrl(alunoData.fotoPerfil)}
                      alt="Foto de perfil"
                      className="w-32 h-32 rounded-full object-cover border-4 border-blue-500 shadow-lg"
                      onError={(e) => {
                        e.target.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="128" height="128"%3E%3Crect width="128" height="128" fill="%23e5e7eb"/%3E%3Ctext x="50%25" y="50%25" text-anchor="middle" dy=".3em" fill="%239ca3af"%3ESem foto%3C/text%3E%3C/svg%3E';
                      }}
                    />
                  ) : (
                    <div className="w-32 h-32 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center border-4 border-blue-500 shadow-lg">
                      <span className="text-white text-4xl font-bold">
                        {alunoData.nome?.charAt(0)?.toUpperCase() || 'A'}
                      </span>
                    </div>
                  )}
                </div>
                <div className="w-full max-w-md">
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    URL da Foto de Perfil
                  </label>
                  <input
                    type="text"
                    name="fotoPerfil"
                    value={alunoData.fotoPerfil}
                    onChange={handleInputChange}
                    placeholder="https://i.imgur.com/..."
                    className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                  />
                  <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
                    Cole o link direto da imagem (recomendado: Imgur com link direto)
                  </p>
                </div>
              </div>

              {/* Nome */}
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Nome Completo *
                </label>
                <input
                  type="text"
                  name="nome"
                  value={alunoData.nome}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                />
              </div>

              {/* Email */}
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Email *
                </label>
                <input
                  type="email"
                  name="email"
                  value={alunoData.email}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                />
              </div>

              {/* CPF e RG */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    CPF *
                  </label>
                  <input
                    type="text"
                    name="cpf"
                    value={alunoData.cpf}
                    onChange={handleInputChange}
                    required
                    pattern="\d{3}\.\d{3}\.\d{3}-\d{2}"
                    placeholder="000.000.000-00"
                    className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    RG *
                  </label>
                  <input
                    type="text"
                    name="rg"
                    value={alunoData.rg}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                  />
                </div>
              </div>

              {/* Endereço */}
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Endereço *
                </label>
                <input
                  type="text"
                  name="endereco"
                  value={alunoData.endereco}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                />
              </div>

              {/* Curso */}
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Curso *
                </label>
                <input
                  type="text"
                  name="curso"
                  value={alunoData.curso}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white dark:bg-gray-700 text-gray-900 dark:text-white"
                />
              </div>

              {/* Saldo de Moedas (somente leitura) */}
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Saldo de Moedas
                </label>
                <div className="px-4 py-2 bg-gray-100 dark:bg-gray-700 rounded-lg text-gray-900 dark:text-white font-semibold">
                  {alunoData.saldoMoedas.toFixed(2)} moedas
                </div>
                <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
                  O saldo não pode ser editado manualmente
                </p>
              </div>

              {/* Botões */}
              <div className="flex justify-end space-x-4 pt-4">
                <button
                  type="button"
                  onClick={() => router.back()}
                  className="px-6 py-2 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  disabled={saving}
                  className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {saving ? 'Salvando...' : 'Salvar Alterações'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </ProtectedRoute>
  );
};

export default PerfilAlunoPage;

