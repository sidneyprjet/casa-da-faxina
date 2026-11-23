
# Guia de Implementação no Android Studio - Casa da Faxina

Este documento fornece as diretrizes, a estrutura de pastas e os códigos base para implementar o aplicativo **Casa da Faxina** no Android Studio, utilizando **Jetpack Compose** (ou XML tradicional, adaptável).

---

## 1. Estrutura de Telas do App

O fluxo principal do aplicativo consiste em 4 telas principais:
1. **Início (`01 • Início`)**: Tela inicial com banner promocional, barra de busca e carrossel de produtos em destaque.
2. **Categorias (`02 • Categorias`)**: Navegação por seções (Pisos, Tecidos, Acessórios) com listagem específica por categoria.
3. **Produto (`03 • Produto`)**: Detalhes do produto selecionado, seletor de quantidade e botão de adição ao carrinho.
4. **Carrinho (`04 • Carrinho`)**: Revisão dos itens adicionados, subtotal correto (sem duplicações) e ícone de carrinho com badge no topo.

---

## 2. Componentes Principais e Regras de Correção Aplicadas
* **Cabeçalho do Carrinho**: Contém o botão de voltar/fechar, o título "Carrinho" e o ícone do carrinho de supermercado com a badge vermelha indicando a quantidade de itens.
* **Subtotal Único**: Garantido o cálculo correto do subtotal dos produtos sem repetição de linhas.
* **Imagens Únicas**: Cada produto exibe corretamente sua respectiva imagem (ex: Limpador para Tapetes e Estofados no primeiro card e Limpador para Laminado no segundo card).

---

## 3. Exemplo de Implementação em Jetpack Compose

Abaixo está o modelo sugerido para a tela do **Carrinho** utilizando Jetpack Compose:

```kotlin
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onReviewOrder: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrinho") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 16.dp)) {
                        IconButton(onClick = { /* Ação do Carrinho */ }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrinho")
                        }
                        // Badge de quantidade
                        Badge(
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text("2")
                        }
                    }
                }
            )
        },
        bottomBar = {
            CartBottomBar(subtotal = "R$ 59,98", onReviewOrder = onReviewOrder)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F7FA))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BannerHeaderCart()
            }
            item {
                CartItemCard(
                    title = "Limpador para Tapetes e Estofados",
                    price = "R$ 29,99",
                    quantity = 1
                )
            }
            item {
                CartItemCard(
                    title = "Limpador para Laminado",
                    price = "R$ 29,99",
                    quantity = 1
                )
            }
        }
    }
}
```

---

## 4. Recursos e Cores Recomendados (`themes.xml` / `Color.kt`)

* **Cor Primária (Azul)**: `#0047AB`
* **Cor de Destaque (Amarelo)**: `#FFD700`
* **Cor de Fundo**: `#F5F7FA`
* **Texto Principal**: `#1A202C`

```xml
<!-- Exemplo de Cores em XML -->
<resources>
    <color name="primary_blue">#0047AB</color>
    <color name="accent_yellow">#FFD700</color>
    <color name="background_light">#F5F7FA</color>
</resources>
```