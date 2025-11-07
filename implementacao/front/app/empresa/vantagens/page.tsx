"use client";

import { useAuth } from "@/contexts/AuthContext";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { ProtectedRoute } from "@/components/Auth/ProtectedRoute";
import toast from "react-hot-toast";

const VantagensPage = () => {
  const { user, token } = useAuth();
  const router = useRouter();
  const [vantagens, setVantagens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('add'); // 'add', 'edit', 'delete'
  const [selectedVantagem, setSelectedVantagem] = useState(null);
  const [showImageModal, setShowImageModal] = useState(false);
  const [selectedImageUrl, setSelectedImageUrl] = useState('');
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    fotoUrl: '',
    custoEmMoedas: ''
  });

  // Função para converter links do Imgur para formato direto
  const getDirectImageUrl = (url) => {
    if (!url) return null;
    
    // Se já é um link direto do Imgur (i.imgur.com)
    if (url.includes('i.imgur.com')) {
      return url;
    }
    
    // Se é um link de galeria/álbum do Imgur (imgur.com/a/ ou imgur.com/gallery/)
    if (url.includes('imgur.com/a/') || url.includes('imgur.com/gallery/')) {
      // Extrair o ID do álbum
      const match = url.match(/imgur\.com\/(?:a|gallery)\/([a-zA-Z0-9]+)/);
      if (match && match[1]) {
        // Tentar obter a primeira imagem do álbum
        // Nota: Para álbuns, precisaríamos da API do Imgur, mas vamos tentar um formato comum
        return `https://i.imgur.com/${match[1]}.jpg`;
      }
    }
    
    // Se é um link simples do Imgur (imgur.com/ID)
    if (url.includes('imgur.com/') && !url.includes('i.imgur.com')) {
      const match = url.match(/imgur\.com\/([a-zA-Z0-9]+)/);
      if (match && match[1]) {
        return `https://i.imgur.com/${match[1]}.jpg`;
      }
    }
    
    // Retornar o URL original se não for Imgur
    return url;
  };

  useEffect(() => {
    if (user && user.roles.includes('EMPRESA')) {
      fetchVantagens();
    } else if (user && !user.roles.includes('EMPRESA')) {
      toast.error('Acesso negado. Apenas empresas podem gerenciar vantagens.');
      router.push('/');
    }
  }, [user, token, router]);

  const fetchVantagens = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/vantagens/minhas', {
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

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const openModal = (type, vantagem = null) => {
    setModalType(type);
    setSelectedVantagem(vantagem);
    
    if (type === 'add') {
      setFormData({
        nome: '',
        descricao: '',
        fotoUrl: '',
        custoEmMoedas: ''
      });
    } else if (type === 'edit' && vantagem) {
      setFormData({
        nome: vantagem.nome || '',
        descricao: vantagem.descricao || '',
        fotoUrl: vantagem.fotoUrl || '',
        custoEmMoedas: vantagem.custoEmMoedas || ''
      });
    }
    
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedVantagem(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const url = modalType === 'add' 
        ? 'http://localhost:8080/api/vantagens'
        : `http://localhost:8080/api/vantagens/${selectedVantagem.id}`;
      
      const method = modalType === 'add' ? 'POST' : 'PUT';
      
      const dataToSend = {
        ...formData,
        custoEmMoedas: parseFloat(formData.custoEmMoedas)
      };
      
      const response = await fetch(url, {
        method: method,
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataToSend)
      });

      if (response.ok) {
        closeModal();
        fetchVantagens();
        toast.success(`Vantagem ${modalType === 'add' ? 'adicionada' : 'atualizada'} com sucesso!`, {
          duration: 3000,
          position: 'top-right',
        });
      } else {
        const errorText = await response.text();
        toast.error(`Erro ao ${modalType === 'add' ? 'adicionar' : 'atualizar'} vantagem: ${errorText}`, {
          duration: 4000,
          position: 'top-right',
        });
      }
    } catch (error) {
      console.error('Erro ao salvar vantagem:', error);
      toast.error(`Erro ao ${modalType === 'add' ? 'adicionar' : 'atualizar'} vantagem`, {
        duration: 4000,
        position: 'top-right',
      });
    }
  };

  const handleDelete = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/vantagens/${selectedVantagem.id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        closeModal();
        fetchVantagens();
        toast.success('Vantagem excluída com sucesso!', {
          duration: 3000,
          position: 'top-right',
        });
      } else {
        const errorText = await response.text();
        toast.error(`Erro ao excluir vantagem: ${errorText}`, {
          duration: 4000,
          position: 'top-right',
        });
      }
    } catch (error) {
      console.error('Erro ao excluir vantagem:', error);
      toast.error('Erro ao excluir vantagem', {
        duration: 4000,
        position: 'top-right',
      });
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
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
            Gerenciar Vantagens
          </h1>
          <button
            onClick={() => router.push('/')}
            className="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
          >
            Voltar
          </button>
        </div>
        
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6">
          <div className="mb-6">
            <button 
              onClick={() => openModal('add')}
              className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
            >
              Adicionar Vantagem
            </button>
          </div>

          <div className="overflow-x-auto">
            <table className="min-w-full bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700">
              <thead className="bg-gray-50 dark:bg-gray-700">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    ID
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Nome
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Descrição
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Custo (Moedas)
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Foto
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                {vantagens.length > 0 ? (
                  vantagens.map((vantagem) => (
                    <tr key={vantagem.id} className="hover:bg-gray-50 dark:hover:bg-gray-700">
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        {vantagem.id}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        {vantagem.nome}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900 dark:text-white max-w-xs truncate">
                        {vantagem.descricao || '-'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        {vantagem.custoEmMoedas}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {vantagem.fotoUrl ? (
                          <img 
                            src={getDirectImageUrl(vantagem.fotoUrl)} 
                            alt={vantagem.nome}
                            className="w-16 h-16 object-cover rounded-md border border-gray-300 dark:border-gray-600 shadow-sm cursor-pointer hover:opacity-80 transition-opacity"
                            onClick={() => {
                              setSelectedImageUrl(getDirectImageUrl(vantagem.fotoUrl));
                              setShowImageModal(true);
                            }}
                            onError={(e) => {
                              // Tentar outras extensões se .jpg falhar
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
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button 
                          onClick={() => openModal('edit', vantagem)}
                          className="text-blue-600 hover:text-blue-900 dark:text-blue-400 dark:hover:text-blue-300 mr-3"
                        >
                          Editar
                        </button>
                        <button 
                          onClick={() => openModal('delete', vantagem)}
                          className="text-red-600 hover:text-red-900 dark:text-red-400 dark:hover:text-red-300"
                        >
                          Excluir
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={6} className="px-6 py-4 text-center text-sm text-gray-500 dark:text-gray-400">
                      Nenhuma vantagem cadastrada
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Modal */}
        {showModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
              {/* Header do Modal */}
              <div className="flex justify-between items-center p-6 border-b border-gray-200 dark:border-gray-700">
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                  {modalType === 'add' && 'Adicionar Nova Vantagem'}
                  {modalType === 'edit' && 'Editar Vantagem'}
                  {modalType === 'delete' && 'Confirmar Exclusão'}
                </h3>
                <button
                  onClick={closeModal}
                  className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300"
                >
                  <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>

              {/* Conteúdo do Modal */}
              <div className="p-6">
                {modalType === 'delete' ? (
                  <div className="text-center">
                    <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100 dark:bg-red-900/20 mb-4">
                      <svg className="h-6 w-6 text-red-600 dark:text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z" />
                      </svg>
                    </div>
                    <h3 className="text-lg font-medium text-gray-900 dark:text-white mb-2">
                      Tem certeza que deseja excluir?
                    </h3>
                    <p className="text-sm text-gray-500 dark:text-gray-400 mb-6">
                      Esta ação não pode ser desfeita. A vantagem <strong>{selectedVantagem?.nome}</strong> será permanentemente removida.
                    </p>
                    <div className="flex justify-center space-x-3">
                      <button
                        onClick={closeModal}
                        className="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 bg-gray-100 dark:bg-gray-700 hover:bg-gray-200 dark:hover:bg-gray-600 rounded-lg transition-colors"
                      >
                        Cancelar
                      </button>
                      <button
                        onClick={handleDelete}
                        className="px-4 py-2 text-sm font-medium text-white bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
                      >
                        Excluir
                      </button>
                    </div>
                  </div>
                ) : (
                  <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        Nome *
                      </label>
                      <input
                        type="text"
                        name="nome"
                        value={formData.nome}
                        onChange={handleInputChange}
                        required
                        className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        Descrição
                      </label>
                      <textarea
                        name="descricao"
                        value={formData.descricao}
                        onChange={handleInputChange}
                        rows={4}
                        maxLength={1000}
                        className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                      />
                      <p className="text-xs text-gray-500 dark:text-gray-400 mt-1">
                        {formData.descricao.length}/1000 caracteres
                      </p>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        URL da Foto
                      </label>
                      <input
                        type="url"
                        name="fotoUrl"
                        value={formData.fotoUrl}
                        onChange={handleInputChange}
                        placeholder="https://exemplo.com/foto.jpg"
                        className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        Custo em Moedas *
                      </label>
                      <input
                        type="number"
                        name="custoEmMoedas"
                        value={formData.custoEmMoedas}
                        onChange={handleInputChange}
                        required
                        min="0.01"
                        step="0.01"
                        className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-800 dark:text-white"
                      />
                    </div>
                    
                    <div className="flex justify-end space-x-3 pt-4">
                      <button
                        type="button"
                        onClick={closeModal}
                        className="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 bg-gray-100 dark:bg-gray-700 hover:bg-gray-200 dark:hover:bg-gray-600 rounded-lg transition-colors"
                      >
                        Cancelar
                      </button>
                      <button
                        type="submit"
                        className="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors"
                      >
                        {modalType === 'add' ? 'Adicionar' : 'Salvar'}
                      </button>
                    </div>
                  </form>
                )}
              </div>
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

export default VantagensPage;

