/** @type {import('next').NextConfig} */
const nextConfig = {
  // Configuração do Turbopack para resolver o problema de workspace root
  experimental: {
    turbo: {
      root: process.cwd(),
    },
  },
  images: {
    // Removido domains (deprecated) e mantido apenas remotePatterns
    remotePatterns: [
      {
        protocol: "http",
        hostname: "localhost",
        port: "8080",
        pathname: "/**",
      },
      {
        protocol: "https",
        hostname: "cdn.sanity.io",
        port: "",
        pathname: "/**",
      },
    ],
  },
};

module.exports = nextConfig;
