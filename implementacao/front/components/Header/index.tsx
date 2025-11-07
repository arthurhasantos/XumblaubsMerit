"use client";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useEffect, useState } from "react";
import ThemeToggler from "./ThemeToggler";
import menuData from "./menuData";
import { useAuth } from "@/contexts/AuthContext";

const Header = () => {
  const { user, logout } = useAuth();
  
  // Função para converter links do Imgur para formato direto
  const getDirectImageUrl = (url: string | undefined) => {
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
  
  // Navbar toggle
  const [navbarOpen, setNavbarOpen] = useState(false);
  const navbarToggleHandler = () => {
    setNavbarOpen(!navbarOpen);
  };

  // Sticky Navbar
  const [sticky, setSticky] = useState(false);
  const handleStickyNavbar = () => {
    if (window.scrollY >= 80) {
      setSticky(true);
    } else {
      setSticky(false);
    }
  };
  useEffect(() => {
    window.addEventListener("scroll", handleStickyNavbar);
  });

  // submenu handler
  const [openIndex, setOpenIndex] = useState(-1);
  const handleSubmenu = (index) => {
    if (openIndex === index) {
      setOpenIndex(-1);
    } else {
      setOpenIndex(index);
    }
  };

  const usePathName = usePathname();

  return (
    <>
      <header
        className={`header left-0 top-0 z-40 flex w-full items-center relative ${
          sticky
            ? "fixed z-[9999] bg-white/95 dark:bg-gray-900/95 backdrop-blur-md shadow-sm transition-all duration-300"
            : "absolute bg-transparent"
        }`}
      >
        {/* Divisão bonita com gradiente */}
        <div className="absolute bottom-0 left-0 right-0 h-px bg-gradient-to-r from-transparent via-gray-300 dark:via-gray-600 to-transparent"></div>
        <div className="container mx-auto px-4">
          <div className="flex items-center justify-between h-16 lg:h-20">
            {/* Logo */}
            <div className="flex-shrink-0 flex items-center">
              <Link
                href="/"
                className="flex items-center space-x-3 group"
              >
                <div className="w-12 h-12 bg-gradient-to-br from-blue-600 to-purple-600 rounded-lg flex items-center justify-center transition-opacity duration-200 group-hover:opacity-90">
                  <span className="text-white font-semibold text-xl">M</span>
                </div>
                <span className="text-xl font-semibold text-gray-900 dark:text-white transition-colors duration-200">
                  Merit
                </span>
              </Link>
            </div>

            {/* Navigation */}
            <div className="hidden lg:flex lg:items-center lg:space-x-6">
              {menuData
                .filter((menuItem) => {
                  if (!user) return false;
                  if (menuItem.requireAdmin) {
                    return user.roles.includes('ADMIN');
                  }
                  if (menuItem.requireEmpresa) {
                    return user.roles.includes('EMPRESA');
                  }
                  if (menuItem.requireAluno) {
                    return user.roles.includes('ALUNO');
                  }
                  return true;
                })
                .map((menuItem, index) => (
                  <Link
                    key={index}
                    href={menuItem.path}
                    className={`relative px-4 py-2.5 text-base font-medium transition-colors duration-200 rounded-lg ${
                      usePathName === menuItem.path
                        ? "text-blue-600 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/20"
                        : "text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-50 dark:hover:bg-gray-800/50"
                    }`}
                  >
                    {menuItem.title}
                  </Link>
                ))}
            </div>

            {/* User Actions */}
            <div className="flex items-center space-x-4">
              {user ? (
                <div className="flex items-center space-x-4">
                  {/* User Info - Clicável para alunos */}
                  {user.roles.includes('ALUNO') ? (
                    <Link
                      href="/aluno/perfil"
                      className="hidden md:flex items-center space-x-3 px-3 py-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors duration-200 cursor-pointer group relative"
                    >
                      {/* Conteúdo normal (imagem e nome) */}
                      <div className="flex items-center space-x-3 transition-opacity duration-200 group-hover:opacity-0">
                        {user.fotoPerfil ? (
                          <img
                            src={getDirectImageUrl(user.fotoPerfil) || user.fotoPerfil}
                            alt={user.name}
                            className="w-12 h-12 rounded-full object-cover border border-gray-300 dark:border-gray-600"
                            onError={(e) => {
                              const target = e.target as HTMLImageElement;
                              target.style.display = 'none';
                              const fallback = target.nextElementSibling as HTMLElement;
                              if (fallback) {
                                fallback.style.display = 'flex';
                              }
                            }}
                          />
                        ) : null}
                        <div 
                          className={`w-12 h-12 bg-gray-200 dark:bg-gray-700 rounded-full flex items-center justify-center ${user.fotoPerfil ? 'hidden' : ''}`}
                          style={{ display: user.fotoPerfil ? 'none' : 'flex' }}
                        >
                          <span className="text-gray-600 dark:text-gray-300 text-base font-medium">
                            {user.name?.charAt(0)?.toUpperCase() || 'A'}
                          </span>
                        </div>
                        <span className="text-base font-medium text-gray-700 dark:text-gray-300">
                          {user.name}
                        </span>
                      </div>
                      {/* Texto "Editar" que aparece no hover */}
                      <span className="text-base font-medium text-gray-700 dark:text-gray-300 absolute left-1/2 top-1/2 -translate-x-[70%] -translate-y-1/2 transition-opacity duration-200 opacity-0 group-hover:opacity-100 whitespace-nowrap">
                        Editar
                      </span>
                    </Link>
                  ) : (
                    <div className="hidden md:flex items-center space-x-3 px-3 py-2">
                      {user.fotoPerfil ? (
                        <img
                          src={getDirectImageUrl(user.fotoPerfil) || user.fotoPerfil}
                          alt={user.name}
                          className="w-12 h-12 rounded-full object-cover border border-gray-300 dark:border-gray-600"
                          onError={(e) => {
                            const target = e.target as HTMLImageElement;
                            target.style.display = 'none';
                            const fallback = target.nextElementSibling as HTMLElement;
                            if (fallback) {
                              fallback.style.display = 'flex';
                            }
                          }}
                        />
                      ) : null}
                      <div 
                        className={`w-12 h-12 bg-gray-200 dark:bg-gray-700 rounded-full flex items-center justify-center ${user.fotoPerfil ? 'hidden' : ''}`}
                        style={{ display: user.fotoPerfil ? 'none' : 'flex' }}
                      >
                        <span className="text-gray-600 dark:text-gray-300 text-base font-medium">
                          {user.name?.charAt(0)?.toUpperCase() || 'A'}
                        </span>
                      </div>
                      <span className="text-base font-medium text-gray-700 dark:text-gray-300">
                        {user.name}
                      </span>
                    </div>
                  )}
                  
                  {/* Logout Button */}
                  <button
                    onClick={logout}
                    className="flex items-center space-x-2 px-4 py-2 text-base font-medium text-red-600 dark:text-red-400 hover:text-red-700 dark:hover:text-red-300 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-colors duration-200"
                  >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <span>Logout</span>
                  </button>
                </div>
              ) : (
                <Link
                  href="/signin"
                  className="flex items-center space-x-2 px-5 py-2.5 text-base font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors duration-200"
                >
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 16l-4-4m0 0l4-4m-4 4h14m-5 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h7a3 3 0 013 3v1" />
                  </svg>
                  <span>Login</span>
                </Link>
              )}

              {/* Theme Toggle */}
              <div className="ml-1">
                <ThemeToggler />
              </div>

              {/* Mobile Menu Button */}
              <button
                onClick={navbarToggleHandler}
                className="lg:hidden p-2 rounded-lg text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors duration-300"
                aria-label="Toggle menu"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  {navbarOpen ? (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  ) : (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                  )}
                </svg>
              </button>
            </div>
          </div>

          {/* Mobile Menu */}
          <div
            className={`lg:hidden transition-all duration-300 ease-in-out ${
              navbarOpen
                ? "max-h-96 opacity-100 pb-4"
                : "max-h-0 opacity-0 overflow-hidden"
            }`}
          >
            <div className="pt-4 border-t border-gray-200 dark:border-gray-700">
              <nav className="flex flex-col space-y-2">
                {menuData
                  .filter((menuItem) => {
                    if (!user) return false;
                    if (menuItem.requireAdmin) {
                      return user.roles.includes('ADMIN');
                    }
                    if (menuItem.requireEmpresa) {
                      return user.roles.includes('EMPRESA');
                    }
                    return true;
                  })
                  .map((menuItem, index) => (
                    <Link
                      key={index}
                      href={menuItem.path}
                      className={`px-4 py-3 text-sm font-medium rounded-lg transition-all duration-300 ${
                        usePathName === menuItem.path
                          ? "text-blue-600 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/20"
                          : "text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-gray-50 dark:hover:bg-gray-800/50"
                      }`}
                      onClick={() => setNavbarOpen(false)}
                    >
                      {menuItem.title}
                    </Link>
                  ))}
              </nav>
            </div>
          </div>
        </div>
      </header>
    </>
  );
};

export default Header;
