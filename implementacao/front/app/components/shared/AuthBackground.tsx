const AuthBackground = () => {
  return (
    <div className="absolute left-0 top-0 z-[-1] h-full w-full">
      <div className="absolute left-0 top-0 h-full w-full bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800"></div>
      <div className="absolute left-0 top-0 h-full w-full bg-[url('/images/shape/shape-1.svg')] bg-cover bg-center bg-no-repeat opacity-10"></div>
    </div>
  );
};

export default AuthBackground;
