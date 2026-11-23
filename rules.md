# Regras de Design e Interface (UI/UX) — Casa da Faxina

Este documento define as diretrizes visuais, estruturais e de componentes para o aplicativo **Casa da Faxina — Produtos de Limpeza**, baseado nas telas de abertura (*Splash Screen*) e autenticação (*Login*).

---

## 1. Identidade Visual e Paleta de Cores

* **Marca:** Casa da Faxina (`Produtos de Limpeza`)
* **Mascote / Logotipo:** Casa estilizada em azul e branco com rosto amigável piscando, mão amarela acenando e detalhes de brilho (estrelas de quatro pontas), acompanhada da faixa vermelha inferior com o subtítulo.

### Cores Principais
* **Azul Primário (Botões e Destaque):** `#0038A8` (Azul Royal vibrante)
* **Azul Escuro (Logotipo e Títulos):** `#0A1F44` / `#002366`
* **Amarelo Destaque (Tipografia da Marca e Mascote):** `#FFD100`
* **Vermelho Faixa (Subtítulo da Logo):** `#E31C25`
* **Branco (Fundo Principal):** `#FFFFFF`
* **Cinza Claro (Campos de Input):** `#F2F3F5`
* **Cinza Texto Secundário / Placeholder:** `#6E7781`
* **Cinza Borda / Divisor:** `#D0D7DE`

---

## 2. Tipografia

* **Família Tipográfica:** Sans-serif moderna e arredondada (ex.: *Inter*, *Poppins* ou *Plus Jakarta Sans*).
* **Hierarquia:**
  * **Título Principal (`Bem-vindo de volta!`):** Negrito (*Bold*), tamanho grande (`24px–28px`), cor Azul Escuro (`#0A1F44`), alinhado ao centro.
  * **Subtítulo (`Vamos cuidar da sua casa?`):** Regular, tamanho médio (`15px–16px`), cor Cinza Escuro (`#4A5568`), alinhado ao centro.
  * **Slogan Splash (`Seu cuidado com a casa, na palma da mão`):** Semi-negrito (*SemiBold*), tamanho médio-grande (`18px–20px`), cor Azul Escuro (`#0A1F44`).
  * **Rodapé Splash (`Tudo para sua casa brilhar.`):** Itálico (*Italic*), tamanho pequeno (`14px`), cor Azul Escuro.
  * **Texto de Botão Primário:** Semi-negrito (*SemiBold*), `16px`, cor Branca (`#FFFFFF`).

---

## 3. Estrutura das Telas

### 3.1. Tela de Abertura (*Splash Screen*)
* **Fundo:** Fotografia realista de ambiente limpo e iluminado (produtos de limpeza, borrifador amarelo/azul, toalhas dobradas e esponja amarela) com leve desfoque (*blur*) e sobreposição clara central para garantir legibilidade.
* **Elemento Central:** Logotipo completo com mascote centralizado verticalmente.
* **Textos Inferiores:**
  1. Chamada principal: *"Seu cuidado com a casa, na palma da mão"*
  2. Frase de apoio no rodapé: *"Tudo para sua casa brilhar."*

### 3.2. Tela de Login (*Autenticação*)
* **Fundo:** Branco sólido (`#FFFFFF`) com barra de status superior padrão.
* **Cabeçalho:** Logotipo com mascote centralizado no topo.
* **Saudação:**
  * Título: **Bem-vindo de volta!**
  * Subtítulo: Vamos cuidar da sua casa?
* **Formulário de Acesso:**
  * **Campo E-mail:** Fundo `#F2F3F5`, bordas arredondadas (`8px–10px`), ícone de envelope à esquerda, placeholder `"E-mail"`.
  * **Campo Senha:** Fundo `#F2F3F5`, bordas arredondadas (`8px–10px`), ícone de cadeado à esquerda, placeholder `"Senha"`, ícone de olho (exibir/ocultar senha) à direita.
  * **Link de Recuperação:** `"Esqueci minha senha"` alinhado à direita abaixo do campo de senha, cor Azul Primário (`#0038A8`).
* **Ações:**
  * **Botão Principal:** Largura total (*full-width*), altura `48px–52px`, bordas arredondadas (`10px`), fundo Azul Primário (`#0038A8`), texto branco `"Entrar"`.
  * **Divisor:** Linha horizontal fina com o texto `"ou"` centralizado.
  * **Botão Social (Google):** Largura total (*full-width*), fundo branco, borda fina cinza (`1px solid #D0D7DE`), bordas arredondadas (`10px`), ícone oficial do Google à esquerda e texto `"Continuar com Google"`.
* **Rodapé de Cadastro:**
  * Texto centralizado: `Ainda não tem conta?` seguido do link em negrito azul **`Cadastre-se`**.

---

## 4. Regras de Implementação (Boas Práticas)

1. **Responsividade:** Utilizar espaçamento lateral (*padding*) consistente de `20px` a `24px` nas bordas da tela.
2. **Acessibilidade:** Garantir contraste adequado nos campos de formulário e suporte a leitores de tela nos ícones (envelope, cadeado e alternância de visibilidade de senha).
3. **Estados Interativos:**
   * Adicionar estado de foco (*focus ring*) azul nos inputs de `E-mail` e `Senha`.
   * Exibir indicador de carregamento (*loading spinner*) no botão `Entrar` durante a autenticação.
