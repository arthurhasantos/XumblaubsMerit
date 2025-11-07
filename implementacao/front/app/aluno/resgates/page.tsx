"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import toast from "react-hot-toast";

const ResgatesAlunoPage = () => {
  const { user, token } = useAuth();
  const router = useRouter();
  const [resgates, setResgates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saldoAluno, setSaldoAluno] = useState(0);
  const [showImageModal, setShowImageModal] = useState(false);
  const [selectedImageUrl, setSelectedImageUrl] = useState('');

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
      fetchResgates();
      fetchSaldoAluno();
    } else if (user && !user.roles.includes('ALUNO')) {
      toast.error('Acesso negado. Apenas alunos podem visualizar resgates.');
      router.push('/');
    }
  }, [user, token, router]);

  const fetchResgates = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/resgates/meus', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        setResgates(data);
      } else {
        console.error('Erro ao buscar resgates:', response.statusText);
        toast.error('Erro ao carregar resgates');
      }
    } catch (error) {
      console.error('Erro ao buscar resgates:', error);
      toast.error('Erro ao carregar resgates');
    } finally {
      setLoading(false);
    }
  };

  const fetchSaldoAluno = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/alunos/me', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const aluno = await response.json();
        setSaldoAluno(aluno.saldoMoedas || 0);
      }
    } catch (error) {
      console.error('Erro ao buscar saldo:', error);
    }
  };

  const formatarData = (dataString) => {
    if (!dataString) return '-';
    try {
      const data = new Date(dataString);
      return data.toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return dataString;
    }
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
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              Meus Resgates
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Histórico de vantagens resgatadas
            </p>
          </div>
          <div className="flex items-stretch space-x-4">
            <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white px-8 py-3 rounded-lg shadow-lg flex flex-row items-center justify-between flex-1">
              <div className="flex flex-col">
                <div className="text-sm font-medium mb-1">Saldo Atual</div>
                <div className="text-xl font-bold">{saldoAluno.toFixed(2)} moedas</div>
              </div>
            </div>
            <button
              onClick={() => router.push('/aluno/vantagens')}
              className="bg-gradient-to-r from-blue-500 to-cyan-500 hover:from-blue-600 hover:to-cyan-600 text-white px-8 py-3 rounded-lg shadow-lg transition-all duration-300 flex items-center justify-center flex-1"
            >
              <div className="text-xl font-medium">Ver Vantagens</div>
            </button>
          </div>
        </div>

        {resgates.length === 0 ? (
          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-12 text-center">
            <svg className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
            <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
              Nenhum resgate encontrado
            </h3>
            <p className="text-gray-500 dark:text-gray-400 mb-6">
              Você ainda não resgatou nenhuma vantagem.
            </p>
            <button
              onClick={() => router.push('/aluno/vantagens')}
              className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-medium transition-colors"
            >
              Ver Vantagens Disponíveis
            </button>
          </div>
        ) : (
          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg overflow-hidden">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
                <thead className="bg-gray-50 dark:bg-gray-700">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Data do Resgate
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Vantagem
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Código do Cupom
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Custo (Moedas)
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      Foto
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                  {resgates.map((resgate) => (
                    <tr key={resgate.id} className="hover:bg-gray-50 dark:hover:bg-gray-700">
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        {formatarData(resgate.dataResgate)}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900 dark:text-white">
                        <div>
                          <div className="font-medium">{resgate.vantagemNome}</div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <span className="px-3 py-1 text-sm font-mono bg-green-100 dark:bg-green-900/30 text-green-800 dark:text-green-300 rounded-md border border-green-300 dark:border-green-700">
                            {resgate.codigoCupom}
                          </span>
                          <button
                            onClick={() => {
                              navigator.clipboard.writeText(resgate.codigoCupom);
                              toast.success('Código copiado para a área de transferência!');
                            }}
                            className="ml-2 text-blue-600 hover:text-blue-800 dark:text-blue-400 dark:hover:text-blue-300"
                            title="Copiar código"
                          >
                            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                            </svg>
                          </button>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        <span className="text-red-600 dark:text-red-400 font-medium">
                          -{resgate.custoEmMoedas} moedas
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {resgate.vantagemFotoUrl ? (
                          <img 
                            src={getDirectImageUrl(resgate.vantagemFotoUrl)} 
                            alt={resgate.vantagemNome}
                            className="w-16 h-16 object-cover rounded-md border border-gray-300 dark:border-gray-600 shadow-sm cursor-pointer hover:opacity-80 transition-opacity"
                            onClick={() => {
                              setSelectedImageUrl(getDirectImageUrl(resgate.vantagemFotoUrl));
                              setShowImageModal(true);
                            }}
                            onError={(e) => {
                              const currentSrc = e.target.src;
                              if (currentSrc.endsWith('.jpg')) {
                                e.target.src = currentSrc.replace('.jpg', '.png');
                              } else if (currentSrc.endsWith('.png')) {
                                e.target.src = currentSrc.replace('.png', '.gif');
                              } else {
                                e.target.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="64" height="64"%3E%3Crect width="64" height="64" fill="%23e5e7eb"/%3E%3Ctext x="50%25" y="50%25" text-anchor="middle" dy=".3em" fill="%239ca3af" font-size="10"%3ESem imagem%3C/text%3E%3C/svg%3E';
                              }
                            }}
                          />
                        ) : (
                          <div className="w-16 h-16 bg-gray-200 dark:bg-gray-700 rounded-md border border-gray-300 dark:border-gray-600 flex items-center justify-center">
                            <span className="text-xs text-gray-400 dark:text-gray-500">Sem foto</span>
                          </div>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Modal para expandir imagem */}
        {showImageModal && (
          <div 
            className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50"
            onClick={() => setShowImageModal(false)}
          >
            <div className="relative max-w-4xl max-h-[90vh] mx-4">
              <button
                onClick={() => setShowImageModal(false)}
                className="absolute -top-10 right-0 text-white hover:text-gray-300 transition-colors"
                aria-label="Fechar"
              >
                <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
              <img 
                src={selectedImageUrl} 
                alt="Imagem expandida"
                className="max-w-full max-h-[90vh] object-contain rounded-lg shadow-2xl"
                onClick={(e) => e.stopPropagation()}
                onError={(e) => {
                  e.target.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="400" height="300"%3E%3Crect width="400" height="300" fill="%23e5e7eb"/%3E%3Ctext x="50%25" y="50%25" text-anchor="middle" dy=".3em" fill="%239ca3af"%3EErro ao carregar imagem%3C/text%3E%3C/svg%3E';
                }}
              />
            </div>
          </div>
        )}
      </div>
    </ProtectedRoute>
  );
};

export default ResgatesAlunoPage;

