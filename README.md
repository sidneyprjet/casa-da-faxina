# 🧼 Casa da Faxina — App de Produtos de Limpeza

Aplicativo Android completo para loja de produtos de limpeza, desenvolvido com **Jetpack Compose**, **Kotlin** e arquitetura moderna. Ideal para portfólio e demonstração profissional para clientes.

---

## 📱 Sobre o App

O **Casa da Faxina** é um aplicativo móvel que permite aos clientes navegar por produtos de limpeza, adicionar ao carrinho, buscar itens, filtrar por categoria/promoções, editar perfil, gerenciar dados conforme a LGPD e finalizar pedidos pelo WhatsApp da loja.

A solução inclui também uma área administrativa protegida por senha para a dona da loja.

---

## ✨ Funcionalidades

| Módulo | O que faz |
|--------|-----------|
| **Vitrine de Produtos** | Lista de produtos com imagens, preços, categorias e descrições |
| **Busca Inteligente** | Busca por nome, categoria ou descrição (sem distinção de acentos) |
| **Filtros** | Promoções, Mais Vendidos e Todos |
| **Carrinho de Compras** | Adicionar, remover itens e finalizar pedido pelo WhatsApp |
| **Perfil do Cliente** | Editar nome e telefone, trocar foto de perfil, visualizar dados |
| **LGPD** | Visualizar termos, editar ou excluir dados locais definitivamente |
| **WhatsApp da Loja** | Atendimento direto com a dona pelo WhatsApp oficial |
| **Área Admin** | Painel de administração protegido por senha master com hash e salt |
| **Vídeo de Apresentação** | Vídeo institucional na tela inicial |

---

## 🛠 Tecnologias Utilizadas

- **Kotlin 1.9+** — Linguagem principal
- **Jetpack Compose** — UI declarativa moderna (Material 3)
- **Navigation 3** — Navegação entre telas com rotas tipadas
- **Room** — Banco de dados local para dados da loja e sessão
- **DataStore / SharedPreferences** — Preferências e sessão do usuário
- **Coroutines + Flow** — Programação assíncrona reativa
- **ViewModel** — Gerenciamento de estado de UI
- **Material Design 3** — Componentes visuais modernos e responsivos
- **CameraX / Galeria nativa** — Seleção de foto de perfil
- **WhatsApp / Instagram Intents** — Integração com apps externos
- **Criptografia PBKDF2** — Hash seguro de senhas administrativas

---

## 📂 Estrutura do Projeto

```
app/src/main/java/com/example/myapplication/
├── ui/
│   ├── home/           # Tela inicial, busca, produtos, carrinho inferior
│   ├── perfil/         # Perfil do cliente, LGPD, edição de dados
│   ├── carrinho/       # Carrinho de compras e finalização
│   ├── categorias/     # Navegação por categorias
│   ├── produto/        # Detalhes do produto
│   ├── admin/          # Área administrativa (login, painel, senha master)
│   ├── cadastro/       # Cadastro e login de cliente
│   └── theme/          # Tema Material 3 (cores, tipografia)
├── components/         # Componentes reutilizáveis (Cabeçalho, Badge, etc.)
├── data/
│   ├── model/          # Modelos de dados (Produto, Usuario, ItemCarrinho)
│   └── local/          # Room database, SessionManager, DAOs
├── util/               # Utilitários (WhatsAppHelper, InstagramHelper, etc.)
└── navigation/         # Configuração de rotas do Navigation 3
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

- [Android Studio](https://developer.android.com/studio) (Giraffe ou superior recomendado)
- JDK 17
- Android SDK API 24 ou superior (recomendado API 34+)
- Um emulador Android ou dispositivo físico com depuração USB ativada

### Passos

1. **Clone este repositório**
   ```bash
   git clone https://github.com/SEU_USUARIO/MyApplication7.git
   cd MyApplication7
   ```

2. **Abra no Android Studio**
   - Arquivo → Abrir → Selecione a pasta `MyApplication7`
   - Aguarde a sincronização do Gradle

3. **Configure o Google Services (opcional)**
   - Se você usar Firebase, adicione o arquivo `google-services.json` na pasta `app/`
   - Este arquivo **não** está versionado por segurança (veja `.gitignore`)

4. **Configure o WhatsApp da loja**
   - Abra `app/src/main/java/com/example/myapplication/util/WhatsAppHelper.kt`
   - Substitua o valor de `numeroSarinhaWhatsApp` pelo número real da loja

5. **Execute o app**
   - Selecione um dispositivo/emulador
   - Clique em ▶️ Run ou pressione `Shift + F10`

---

## 🔐 Segurança e Privacidade

- Senhas administrativas são armazenadas com **hash PBKDF2 + salt** (nunca em texto puro)
- Dados dos clientes ficam **apenas no dispositivo local** (conforme LGPD)
- Nenhum dado pessoal real está incluído neste repositório (todos os números e nomes são placeholders)
- Arquivos sensíveis (`google-services.json`, `local.properties`, `.keystore`, `.jks`) são ignorados pelo Git

---

## 📄 Licença

Este projeto é distribuído para fins de portfólio profissional. Sinta-se à vontade para estudar, adaptar e usar como referência para seus próprios projetos Android.

---

## 👨‍💻 Autor

Desenvolvido com ❤️ para demonstrar boas práticas de desenvolvimento Android moderno.

---

## 📸 Pré-visualização

*(Adicione capturas de tela das telas principais aqui para enriquecer seu portfólio)*
