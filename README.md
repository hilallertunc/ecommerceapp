# ecommerceapp
E-Commerce Spring Boot API
Spring Boot ile geliştirilmiş RESTful e-ticaret uygulaması.
Özellikler
•	Ürün yönetimi (CRUD işlemleri)
•	Müşteri kaydı ve yönetimi
•	Alışveriş sepeti işlemleri
•	Sipariş oluşturma ve takibi
•	Otomatik stok kontrolü
Teknolojiler
•	Java 11
•	Spring Boot 2.7.0
•	Spring Data JPA
•	Spring Validation
•	H2 Database
•	Maven
API Endpoints
Ürün İşlemleri
•	GET /api/products - Tüm ürünleri listele
•	GET /api/products/{id} - Ürün detayları
•	POST /api/products - Yeni ürün ekle
•	PUT /api/products/{id} - Ürün güncelle
•	DELETE /api/products/{id} - Ürün sil
Müşteri İşlemleri
•	POST /api/customers - Yeni müşteri kaydet
Sepet İşlemleri
•	GET /api/customers/{customerId}/cart - Sepeti görüntüle
•	POST /api/customers/{customerId}/cart/items - Sepete ürün ekle
•	PUT /api/customers/{customerId}/cart/items/{productId} - Sepet güncelle
•	DELETE /api/customers/{customerId}/cart/items/{productId} - Üründen sepeti çıkar
•	DELETE /api/customers/{customerId}/cart - Sepeti boşalt
Sipariş İşlemleri
•	POST /api/customers/{customerId}/orders - Sipariş oluştur
•	GET /api/orders/{orderId} - Sipariş detayları
•	GET /api/customers/{customerId}/orders - Müşteri siparişleri

