function checkEmail(email) {
  // Formatação: usuario@gmail.com (Renan)
  const emailRegex = /^[a-zA-Z0-9._%+-]+@(gmail|icloud|hotmail|yahoo)\.com$/;
  return emailRegex.test(email);
}

function checkName(name) {
  // Formatação: Começar com letra maiúscula, ter mínimo de 3 letras e conter apenas letras (Renan)
  const nameRegex = /^([A-Z][a-zA-Z]{2,})(\s[A-Z][a-zA-Z]{2,})*$/;
  return nameRegex.test(name);
}

function checkPassword(password) {
  // Formatação: Mínimo de 8 caracteres e máximo de 50, deve possuir ao menos -> uma letra minúscula, uma maiúscula, um símbolo e um número (Renan)
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,50}$/;
  return passwordRegex.test(password);
}

function checkConfirmPassword(password, confirmPassword) {
  return password === confirmPassword;
}

export { checkEmail, checkName, checkPassword, checkConfirmPassword };