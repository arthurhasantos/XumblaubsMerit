"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { AuthBackground } from "@/app/components/shared";
import toast from "react-hot-toast";

interface SignupForm {
  email: string;
  password: string;
  confirmPassword: string;
}

const SignupPage = () => {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  
  const {
    register,
    handleSubmit,
    formState: { errors },
    watch
  } = useForm<SignupForm>();

  const password = watch("password");

  const onSubmit = async (data: SignupForm) => {
    setIsLoading(true);
    
    // Simular cadastro (você pode implementar a lógica real aqui)
    setTimeout(() => {
      toast.success('Cadastro realizado com sucesso!');
      router.push('/signin');
      setIsLoading(false);
    }, 1000);
  };

  return (
    <>
      <section className="relative z-10 overflow-hidden pb-16 pt-36 md:pb-20 lg:pb-28 lg:pt-[180px]">
        <div className="container">
          <div className="-mx-4 flex flex-wrap">
            <div className="w-full px-4">
              <div className="shadow-three mx-auto max-w-[500px] rounded bg-white px-6 py-10 dark:bg-dark sm:p-[60px]">
                <h3 className="mb-3 text-center text-2xl font-bold text-black dark:text-white sm:text-3xl">
                  Criar Conta
                </h3>
                <p className="mb-11 text-center text-base font-medium text-body-color">
                  Crie sua conta para acessar o sistema.
                </p>
                <form onSubmit={handleSubmit(onSubmit)}>
                  <div className="mb-8">
                    <label
                      htmlFor="email"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      Email
                    </label>
                    <input
                      type="email"
                      {...register("email", { 
                        required: "Email é obrigatório",
                        pattern: {
                          value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                          message: "Email inválido"
                        }
                      })}
                      placeholder="Digite seu email"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300 focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary dark:focus:shadow-none"
                    />
                    {errors.email && (
                      <p className="mt-1 text-sm text-red-500">{errors.email.message}</p>
                    )}
                  </div>
                  
                  <div className="mb-8">
                    <label
                      htmlFor="password"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      Senha
                    </label>
                    <input
                      type="password"
                      {...register("password", { 
                        required: "Senha é obrigatória",
                        minLength: {
                          value: 6,
                          message: "Senha deve ter pelo menos 6 caracteres"
                        }
                      })}
                      placeholder="Digite sua senha"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300 focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary dark:focus:shadow-none"
                    />
                    {errors.password && (
                      <p className="mt-1 text-sm text-red-500">{errors.password.message}</p>
                    )}
                  </div>
                  
                  <div className="mb-8">
                    <label
                      htmlFor="confirmPassword"
                      className="mb-3 block text-sm text-dark dark:text-white"
                    >
                      Confirmar Senha
                    </label>
                    <input
                      type="password"
                      {...register("confirmPassword", { 
                        required: "Confirmação de senha é obrigatória",
                        validate: value => value === password || "As senhas não coincidem"
                      })}
                      placeholder="Confirme sua senha"
                      className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300 focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary dark:focus:shadow-none"
                    />
                    {errors.confirmPassword && (
                      <p className="mt-1 text-sm text-red-500">{errors.confirmPassword.message}</p>
                    )}
                  </div>
                  
                  <div className="mb-6">
                    <button 
                      type="submit"
                      disabled={isLoading}
                      className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300 hover:bg-primary/90 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      {isLoading ? 'Criando conta...' : 'Criar Conta'}
                    </button>
                  </div>
                  
                  <div className="text-center">
                    <p className="text-sm text-body-color">
                      Já tem uma conta?{" "}
                      <a
                        href="/signin"
                        className="text-primary hover:underline"
                      >
                        Faça login
                      </a>
                    </p>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
        <AuthBackground />
      </section>
    </>
  );
};

export default SignupPage;
