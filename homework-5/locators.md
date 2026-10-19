# Домашнее задание 5. Селекторы и локаторы

### 1.1. Список названий товаров

CSS:
```css
.product-card > h4
```

### 1.2. Список цен товаров

CSS:
```css
.product-card > div:nth-of-type(1)
```

### 1.3. Название товара с ценой 25 (добавьте через админку)

XPath:
```xpath
//div[contains(concat(' ', normalize-space(@class), ' '), ' product-card ')][.//div[number(translate(normalize-space(.), '₽', '')) = 25]]/h4
```

### 1.4. Цена товара с названием «Стакан» (добавьте через админку)

XPath:
```xpath
//div[contains(concat(' ', normalize-space(@class), ' '), ' product-card ')][.//h4[normalize-space()='Стакан']]/div[contains(@style, 'font-weight:bold')]
```

### 1.5. Список карточек товаров в корзине

CSS:
```css
#cart-items .cart-item
```

### 1.6. Кнопка корзины

CSS:
```css
#open-cart-btn
```
