"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import toast from "react-hot-toast";

const VantagensAlunoPage = () => {
  const { user, token } = useAuth();
  const router = useRouter();
  const [vantagens, setVantagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showImageModal, setShowImageModal] = useState(false);
  const [selectedImageUrl, setSelectedImageUrl] = useState('');
  const [saldoAluno, setSaldoAluno] = useState(0);

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
      fetchVantagens();
      fetchSaldoAluno();
    } else if (user && !user.roles.includes('ALUNO')) {
      toast.error('Acesso negado. Apenas alunos podem visualizar vantagens.');
      router.push('/');
    }
  }, [user, token, router]);

  const fetchVantagens = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/vantagens', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        setVantagens(data);
      } else {
        console.error('Erro ao buscar vantagens:', response.statusText);
        toast.error('Erro ao carregar vantagens');
      }
    } catch (error) {
      console.error('Erro ao buscar vantagens:', error);
      toast.error('Erro ao carregar vantagens');
    } finally {
      setLoading(false);
    }
  };

  const fetchSaldoAluno = async () => {
    try {
      // Buscar dados do próprio aluno autenticado
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

  const handleResgatarVantagem = async (vantagemId) => {
    try {
      const response = await fetch('http://localhost:8080/api/resgates', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ vantagemId: vantagemId })
      });

      if (response.ok) {
        const resgate = await response.json();
        toast.success(
          `Vantagem resgatada com sucesso! Código do cupom: ${resgate.codigoCupom}`,
          { duration: 5000 }
        );
        // Atualizar saldo e lista de vantagens
        fetchSaldoAluno();
        fetchVantagens();
      } else {
        const errorText = await response.text();
        toast.error(`Erro ao resgatar vantagem: ${errorText}`, { duration: 4000 });
      }
    } catch (error) {
      console.error('Erro ao resgatar vantagem:', error);
      toast.error('Erro ao resgatar vantagem', { duration: 4000 });
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
              Vantagens Disponíveis
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              Resgate vantagens usando suas moedas
            </p>
          </div>
          <div className="flex items-center space-x-4">
            <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white px-6 py-4 rounded-lg shadow-lg">
              <div className="text-sm font-medium mb-1">Seu Saldo</div>
              <div className="text-2xl font-bold">{saldoAluno.toFixed(2)} moedas</div>
            </div>
            <button
              onClick={() => router.push('/aluno/resgates')}
              className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
            >
              Meus Resgates
            </button>
          </div>
        </div>

        {vantagens.length === 0 ? (
          <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-12 text-center">
            <svg className="mx-auto h-12 w-12 text-gray-400 dark:text-gray-500 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
            </svg>
            <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
              Nenhuma vantagem disponível
            </h3>
            <p className="text-gray-500 dark:text-gray-400">
              Não há vantagens cadastradas no momento.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {vantagens.map((vantagem) => {
              const podeResgatar = saldoAluno >= vantagem.custoEmMoedas;
              return (
                <div
                  key={vantagem.id}
                  className="bg-white dark:bg-gray-800 rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition-shadow duration-300"
                >
                  {/* Imagem */}
                  {vantagem.fotoUrl ? (
                    <div 
                      className="relative h-48 bg-gray-200 dark:bg-gray-700 cursor-pointer"
                      onClick={() => {
                        setSelectedImageUrl(getDirectImageUrl(vantagem.fotoUrl));
                        setShowImageModal(true);
                      }}
                    >
                      <img
                        src={getDirectImageUrl(vantagem.fotoUrl)}
                        alt={vantagem.nome}
                        className="w-full h-full object-cover"
                        onError={(e) => {
                          const currentSrc = e.target.src;
                          if (currentSrc.endsWith('.jpg')) {
                            e.target.src = currentSrc.replace('.jpg', '.png');
                          } else if (currentSrc.endsWith('.png')) {
                            e.target.src = currentSrc.replace('.png', '.gif');
                          }
                        }}
                      />
                      <div className="absolute inset-0 bg-black bg-opacity-0 hover:bg-opacity-10 transition-opacity flex items-center justify-center">
                        <svg className="w-8 h-8 text-white opacity-0 hover:opacity-100 transition-opacity" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v3m0 0v3m0-3h3m-3 0H7" />
                        </svg>
                      </div>
                    </div>
                  ) : (
                    <div className="h-48 bg-gray-200 dark:bg-gray-700 flex items-center justify-center">
                      <svg className="w-16 h-16 text-gray-400 dark:text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                      </svg>
                    </div>
                  )}

                  {/* Conteúdo */}
                  <div className="p-6">
                    <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">
                      {vantagem.nome}
                    </h3>
                    {vantagem.descricao && (
                      <p className="text-gray-600 dark:text-gray-400 mb-4 line-clamp-3">
                        {vantagem.descricao}
                      </p>
                    )}
                    
                    <div className="flex items-center justify-between mb-4">
                      <div>
                        <span className="text-sm text-gray-500 dark:text-gray-400">Custo:</span>
                        <span className={`text-lg font-bold ml-2 ${podeResgatar ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'}`}>
                          {vantagem.custoEmMoedas} moedas
                        </span>
                      </div>
                    </div>

                    <button
                      disabled={!podeResgatar}
                      className={`w-full py-2 px-4 rounded-lg font-medium transition-colors ${
                        podeResgatar
                          ? 'bg-blue-600 hover:bg-blue-700 text-white'
                          : 'bg-gray-300 dark:bg-gray-700 text-gray-500 dark:text-gray-400 cursor-not-allowed'
                      }`}
                      onClick={() => handleResgatarVantagem(vantagem.id)}
                    >
                      {podeResgatar ? 'Resgatar Vantagem' : 'Saldo Insuficiente'}
                    </button>
                  </div>
                </div>
              );
            })}
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

export default VantagensAlunoPage;

