# Диаграмма вариантов использования

![Диаграмма вариантов использования](https://github.com/christmaaas/family-budget-manager/blob/main/docs/diagrams/images/UseCaseDiagram.png) 

# Глоссарий

| Термин | Определение |
|:--|:--|
| Пользователь | Человек, прошедший авторизацию и использующий приложение |
| Бюджет | Сумма средств, доступных пользователю для распределения и учёта |
| Период | Промежуток времени, определяющий время актуальности текущего бюджета |
| Транзакция | Любое финансовое действие пользователя, включающее доходы и расходы |
| Статистика | Сводка информации о произведенных манипуляциях с бюджетом |

# Поток событий 

# Содержание
1 [Актёры](#actors)  
2 [Варианты использования](#use_case)  
2.1 [Войти в учётную запись](#sign_in_to_your_account)  
2.2 [Зарегистрироваться](#sign_up)  
2.3 [Установить бюджет](#set_budget)  
2.4 [Добавить транзакцию](#add_transaction)  
2.5 [Просмотреть историю транзакций](#view_transaction_history)  
2.6 [Просмотреть информацию о транзакции](#view_transaction_information)  
2.7 [Удалить транзакцию](#delete_transaction)  
2.8 [Просмотреть статистику](#view_statistic)  
2.9 [Просмотреть информацию об аккаунте](#view_account_information) 
2.10 [Выйти из учетной записи](#sign_out_of_your_account)

<a name="actors"/>

# 1 Актёры

| Актёр | Описание |
|:--|:--|
| Пользователь | Человек, прошедший авторизацию и использующий приложение |

<a name="use_case"/>

# 2 Варианты использования

<a name="sign_in_to_your_account"/>

## 2.1 Войти в учётную запись

**Описание.** Вариант использования "Войти в свою учётную запись" позволяет пользователю войти в учётную запись для работы с семейным бюджетом.  
**Предусловия.** Пользователь выбрал способ "Sign in" для входа в приложение.  
**Основной поток.**
1. Приложение отображает окно входа в учётную запись;
2. Пользователь вводит свои данные (логин и пароль);
3. Приложение проверяет правильность введённых данных;
4. Приложение предоставляет доступ к учётной записи;
5. Вариант использования завершается.

**Альтернативный поток А1.**
1. Приложение отображает сообщение о некорректных данных;
2. Приложение запрашивает другой ввод;

**Постусловия.** Пользователь может управлять семейным бюджетом.

<a name="sign_up"/>

## 2.2 Зарегистрироваться

**Описание.** Вариант использования "Зарегистрироваться" позволяет пользователю создать свою учётную запись в приложении.  
**Предусловия.** Пользователь выбрал способ "Sign up" для регистрации в приложении.  
**Основной поток.**
1. Приложение отображает окно регистрации;
2. Пользователь вводит свои данные (имя, пароль, почту);
3. Приложение проверяет данные и создаёт учётную запись;
4. Приложение присваивает пользователю статус "зарегистрирован";
5. Вариант использования завершается.

**Альтернативный поток А2.**
1. Приложение уведомляет пользователя о существующем аккаунте;
2. Приложение запрашивает другой ввод;

**Постусловия.** Пользователь может управлять семейным бюджетом.

<a name="set_budget"/>

## 2.3 Установить бюджет

**Описание.** Вариант использования "Установить бюджет" позволяет пользователю ввести данные для установки семейного бюджета.  
**Предусловия.** Пользователь вошёл в приложение по существующей учетной записи либо через регистрацию.  
**Основной поток.**
1. Пользователь нажимает кнопку "Set Budget";
2. Приложение запрашивает значение бюджета и периода;
3. Пользователь вводит значение бюджета и периода;
4. Приложение сохраняет данные и обновляет информацию о бюджете и периоде;
5. Вариант использования завершается.

**Альтернативный поток А3.**
1. Приложение отображает сообщение о некорректных данных;
2. Приложение запрашивает другой ввод;

<a name="add_transaction"/>

## 2.4 Добавить транзакцию

**Описание.** Вариант использования "Добавить транзакцию" позволяет пользователю ввести данные о доходе или расходе.  
**Предусловия.** Пользователь установил новый или использует старый бюджет.  
**Основной поток.**
1. Пользователь нажимает кнопку "Add new transaction";
2. Приложение запрашивает название, сумму, тип транзакции, дату и описание;
3. Пользователь вводит название, сумму, тип транзакции, дату и описание;
4. Приложение сохраняет транзакцию и обновляет бюджет;
5. Вариант использования завершается.

**Альтернативный поток А4.**
1. Приложение отображает сообщение о некорректных данных;
2. Приложение запрашивает другой ввод;

<a name="view_transaction_history"/>

## 2.5 Просмотреть историю транзакций

**Описание.** Вариант использования "Просмотреть историю транзакций" позволяет пользователю просматривать список всех финансовых операций.  
**Предусловия.** Пользователь вошёл в приложение.  
**Основной поток.**
1. Приложение отображает историю транзакций по категориям;
2. Пользователь выбирает категорию транзакций;
3. Пользователь может фильтровать транзакции по сумме;
4. Вариант использования завершается.

<a name="view_transaction_information"/>

## 2.6 Просмотреть информацию о транзакции

**Описание.** Вариант использования "Просмотреть информацию о транзакции" позволяет пользователю посмотреть подробную информацию о выбранной транзакции.  
**Предусловия.** Пользователь открыл историю транзакций.  
**Основной поток.**
1. Пользователь выбирает транзакцию;
2. Приложение отображает окно с подробной информацией о транзакции;
3. Вариант использования завершается.

<a name="delete_transaction"/>

## 2.7 Удалить транзакцию

**Описание.** Вариант использования "Удалить транзакцию" позволяет пользователю удалить транзакцию из истории.  
**Предусловия.** Пользователь открыл историю транзакций.  
**Основной поток.**
1. Пользователь зажимает выбранную транзакцию в истории транзакций;
2. Приложение отображает меню управления транзакцией
3. Пользователь нажимает кнопку "Удалить";
4. Приложение удаляет транзакцию из истории транзакций;
5. Вариант использования завершается.

<a name="view_statistic"/>

## 2.8 Просмотреть статистику

**Описание.** Вариант использования "Просмотреть статистику" позволяет пользователю просматривать графики и отчёты по своим расходам и доходам.  
**Предусловия.** Пользователь добавил несколько транзакций либо установил новый бюджет.  
**Основной поток.**
1. Пользователь выбирает в меню приложения окно "Statisctic";
2. Приложение отображает графики и статистику по расходам и доходам по категориям транзакций;
3. Вариант использования завершается.

<a name="view_account_information"/>

## 2.9 Просмотреть информацию об аккаунте

**Описание.** Вариант использования "Просмотреть информацию об аккаунте" позволяет авторизованному пользователю просмотреть информацию о его аккаунте.  
**Предусловия.** Пользователь выбирает в меню приложения окно "Profile".  
**Основной поток.**
1. Пользователь нажимает на кнопку "Account info";
1. Приложение скрывает главное окно;
2. Приложение отображает окно с информацией об аккаунте;
3. Вариант использования завершается.

<a name="sign_out_of_your_account"/>

## 2.10 Выйти из учетной записи

**Описание.** Вариант использования "Выйти из учётной записи" позволяет авторизованному пользователю выйти из системы.  
**Предусловия.** Пользователь выбирает в меню приложения окно "Profile".  
**Основной поток.**
1. Пользователь нажимает на кнопку "Log out";
1. Приложение скрывает главное окно;
2. Приложение отображает окно выбора способа входа в приложение;
3. Вариант использования завершается.
