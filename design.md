# Design System - Casa da Faxina (Produtos de Limpeza)

## 1. Identidade Visual e Atmosfera
- **Conceito:** Moderno, limpo, acolhedor, confiável e com visual polido.
- **Mascote:** Casinha azul sorridente com telhado azul vibrante, detalhes em amarelo e brilho de limpeza.

## 2. Paleta de Cores (`Color.kt`)
- **Azul Principal (`AzulPrincipal`):** `#0038A8` (Botões primários, links de destaque, elementos da marca).
- **Azul Escuro (`AzulEscuro`):** `#002266` (Bordas do logotipo, estados pressionados).
- **Amarelo Destaque (`AmareloDestaque`):** `#FFD100` (Acentos do logotipo, badges promocionais, destaques).
- **Vermelho Faixa (`VermelhoFaixa`):** `#E31C25` (Subtítulo "PRODUTOS DE LIMPEZA", alertas e ofertas).
- **Fundo Principal (`FundoBranco`):** `#FFFFFF` (Fundo limpo das telas internas).
- **Fundo de Campos (`FundoInput`):** `#F2F3F5` (Preenchimento suave dos campos de formulário).
- **Texto Principal (`TextoEscuro`):** `#1A1A1A` (Títulos e textos de alta leitura).
- **Texto Secundário (`TextoSecundario`):** `#6E7179` (Subtítulos, placeholders e textos de apoio).
- **Borda Sutil (`BordaCinza`):** `#D1D5DB` (Contorno de botões secundários e divisores).

## 3. Tipografia
- **Títulos Principais:** Negrito (`FontWeight.Bold`), `24.sp` a `28.sp`, cor `TextoEscuro`.
- **Subtítulos e Slogans:** Regular ou Médio (`FontWeight.Normal` / `Medium`), `14.sp` a `16.sp`, cor `TextoSecundario`.
- **Textos de Botões:** Negrito (`FontWeight.Bold`), `16.sp`, espaçamento confortável.
- **Links e Ações Rápidas:** Médio ou Negrito, `13.sp` a `14.sp`, cor `AzulPrincipal`.

## 4. Componentes Padrão
- **Botão Primário:** Altura `52.dp`, cantos arredondados (`RoundedCornerShape(12.dp)`), fundo `AzulPrincipal`, texto branco.
- **Botão Secundário (ex: Google):** Altura `52.dp`, cantos arredondados (`12.dp`), fundo branco, borda `1.dp` na cor `BordaCinza`, texto `TextoEscuro`.
- **Campos de Texto (Inputs):** Cantos arredondados (`12.dp`), fundo `FundoInput`, sem linha inferior visível, ícone à esquerda na cor `TextoSecundario` e padding interno confortável.
- **Espaçamento Padrão:** Margens laterais das telas de `24.dp`. Espaçamento entre campos de `14.dp` a `16.dp`.

## 5. Especificação das Telas Base
### Tela 1: Splash Screen (Abertura)
- **Fundo:** Ambiente claro e iluminado remetendo a limpeza e frescor, com desfoque suave.
- **Centro:** Logotipo oficial "CASA DA FAXINA - PRODUTOS DE LIMPEZA" com o mascote da casinha azul em destaque.
- **Texto Central:** *"Seu cuidado com a casa, na palma da mão"* em destaque (`TextoEscuro`, negrito).
- **Rodapé:** *"Tudo para sua casa brilhar."* em itálico suave.
- **Comportamento:** Exibe a identidade por 2 segundos e navega automaticamente para a Tela de Login.

### Tela 2: Login (`LoginScreen`)
- **Topo:** Logo "Casa da Faxina" centralizado (`~150.dp` a `160.dp`).
- **Cabeçalho:** Título *"Bem-vindo de volta!"* + subtítulo *"Vamos cuidar da sua casa?"*.
- **Formulário:**
  - Campo **E-mail** (com ícone de envelope à esquerda).
  - Campo **Senha** (com ícone de cadeado à esquerda e botão de olho à direita para alternar visibilidade).
  - Link alinhado à direita: *"Esqueci minha senha"*.
- **Ações:**
  - Botão primário **"Entrar"**.
  - Divisor horizontal com o texto *"ou"* no centro.
  - Botão secundário **"Continuar com Google"** com ícone oficial colorido.
- **Rodapé:** Texto *"Ainda não tem conta?"* seguido do link em negrito **"Cadastre-se"**.
