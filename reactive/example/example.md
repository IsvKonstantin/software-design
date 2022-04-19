Команды:

`curl -X GET "http://localhost:8080/create-user?id=...&username=...&currency=..."`

`curl -X GET "http://localhost:8080/get-users"`

`curl -X GET "http://localhost:8080/add-product?name=...&price=..."`

`curl -X GET "http://localhost:8080/get-products?id=..."`

Пример исполнения:

```
C:\Users\Flexatroid>curl -X GET "http://localhost:8080/create-user?id=1&username=Chovy&currency=USD"
Created user: [1:Chovy:USD]
C:\Users\Flexatroid>curl -X GET "http://localhost:8080/create-user?id=2&username=Faker&currency=EUR"
Created user: [2:Faker:EUR]
C:\Users\Flexatroid>curl -X GET "http://localhost:8080/create-user?id=3&username=Midbeast&currency=RUB"
Created user: [3:Midbeast:RUB]

C:\Users\Flexatroid>curl -X GET "http://localhost:8080/get-users"
1:Chovy:USD
2:Faker:EUR
3:Midbeast:RUB

C:\Users\Flexatroid>curl -X GET "http://localhost:8080/add-product?name=goredrinker&price=3300"
Added product: [goredrinker:3300.0]
C:\Users\Flexatroid>curl -X GET "http://localhost:8080/add-product?name=tiamat&price=1200"
Added product: [tiamat:1200.0]
C:\Users\Flexatroid>curl -X GET "http://localhost:8080/add-product?name=tiamat&price=1200"
Added product: [tiamat:1200.0]
C:\Users\Flexatroid>curl -X GET "http://localhost:8080/add-product?name=pickaxe&price=874.9"
Added product: [pickaxe:874.9]

C:\Users\Flexatroid>curl -X GET "http://localhost:8080/get-products?id=1"
Product: goredrinker
Price: 40.86687306501548 USD
Product: tiamat
Price: 14.860681114551083 USD
Product: pickaxe
Price: 10.83467492260062 USD

C:\Users\Flexatroid>curl -X GET "http://localhost:8080/get-products?id=2"
Product: goredrinker
Price: 36.9044956385596 EUR
Product: tiamat
Price: 13.419816595839857 EUR
Product: pickaxe
Price: 9.78416461641691 EUR

C:\Users\Flexatroid>curl -X GET "http://localhost:8080/get-products?id=3"
Product: goredrinker
Price: 3300.0 RUB
Product: tiamat
Price: 1200.0 RUB
Product: pickaxe
Price: 874.9 RUB

C:\Users\Flexatroid>curl -X GET "http://localhost:8080/invalid
Invalid request
```