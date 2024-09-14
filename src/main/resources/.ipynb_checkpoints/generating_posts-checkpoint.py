import requests

product2 = {
    "id": 0,
    "price": 120.50,
    "name": "Ocean Breeze Knitted Blanket",
    "post": 1,
    "orders": [],
    "carts": []
}

post2 = {
    "id": 0,
    "name": product2['name'],
    "views": 0,
    "description": "The Ocean Breeze Knitted Blanket features a soft, airy design, inspired by the calming hues of the ocean. Perfect for cozying up on chilly nights or adding texture to your decor.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product3 = {
    "id": 0,
    "price": 89.99,
    "name": "Sunset Clay Mug Set",
    "post": 0,
    "orders": [],
    "carts": []
}

post3 = {
    "id": 0,
    "name": product3['name'],
    "views": 0,
    "description": "A set of handcrafted clay mugs inspired by the vibrant hues of a sunset. These mugs are perfect for serving warm beverages while adding a touch of artistry to your kitchen.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product4 = {
    "id": 0,
    "price": 75.25,
    "name": "Handwoven Macrame Wall Hanging",
    "post": 0,
    "orders": [],
    "carts": []
}

post4 = {
    "id": 0,
    "name": product4['name'],
    "views": 0,
    "description": "This handcrafted macrame wall hanging adds a bohemian touch to any space. Made from natural cotton fibers, it's a perfect addition to your living room or bedroom decor.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product5 = {
    "id": 0,
    "price": 45.00,
    "name": "Rustic Clay Candle Holders",
    "post": 0,
    "orders": [],
    "carts": []
}

post5 = {
    "id": 0,
    "name": product5['name'],
    "views": 0,
    "description": "These handmade clay candle holders feature a rustic finish, perfect for adding a warm, earthy ambiance to your space. Each holder is unique and crafted with care.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product6 = {
    "id": 0,
    "price": 135.75,
    "name": "Vintage-Inspired Hand-Painted Vase",
    "post": 0,
    "orders": [],
    "carts": []
}

post6 = {
    "id": 0,
    "name": product6['name'],
    "views": 0,
    "description": "A vintage-inspired vase, hand-painted with intricate floral patterns. This vase is perfect as a centerpiece or to showcase a fresh bouquet of flowers.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product7 = {
    "id": 0,
    "price": 60.99,
    "name": "Earthy Knitted Cushion Cover",
    "post": 0,
    "orders": [],
    "carts": []
}

post7 = {
    "id": 0,
    "name": product7['name'],
    "views": 0,
    "description": "A hand-knitted cushion cover in earthy tones, adding a cozy and natural touch to any seating area. Made from soft, eco-friendly yarn, it's both stylish and comfortable.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product8 = {
    "id": 0,
    "price": 180.00,
    "name": "Handcrafted Wooden Jewelry Box",
    "post": 0,
    "orders": [],
    "carts": []
}

post8 = {
    "id": 0,
    "name": product8['name'],
    "views": 0,
    "description": "This handcrafted wooden jewelry box is both functional and elegant. With intricate carvings, it’s a perfect place to store your treasured accessories.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product9 = {
    "id": 0,
    "price": 55.50,
    "name": "Delicate Lace Doily Set",
    "post": 0,
    "orders": [],
    "carts": []
}

post9 = {
    "id": 0,
    "name": product9['name'],
    "views": 0,
    "description": "A set of delicate, hand-crocheted lace doilies, perfect for decorating tables, shelves, or adding a vintage charm to any room. Made with fine cotton threads.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product10 = {
    "id": 0,
    "price": 250.00,
    "name": "Abstract Hand-Painted Canvas",
    "post": 0,
    "orders": [],
    "carts": []
}

post10 = {
    "id": 0,
    "name": product10['name'],
    "views": 0,
    "description": "This abstract hand-painted canvas brings modern art into your home. The vibrant colors and bold strokes make it a statement piece in any living space.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

product11 = {
    "id": 0,
    "price": 70.99,
    "name": "Handwoven Straw Basket",
    "post": 0,
    "orders": [],
    "carts": []
}

post11 = {
    "id": 0,
    "name": product11['name'],
    "views": 0,
    "description": "A durable handwoven straw basket that combines functionality with rustic charm. Perfect for storage or as a decorative piece in any room.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}


product1 = {
    "id": 0,
    "price": 000.99,
    "name": "Sunrise Clay Vase",
    "post": 0,
    "orders": [],
    "carts": []
}


post1 = {
    "id": 0,
    "name": product1['name'],
    "views": 0,
    "description": "The Sunrise Clay Vase is a handcrafted piece with a warm, earthy tone, inspired by sunrise hues. Its elegant, minimalist design adds a natural touch to any interior decor. Ideal for flowers or as a statement piece.",
    "product": 0,
    "user": 0,
    "likedUsers": [],
    "tags": [],
    "images": [],
    "reviews": []
}

posts = [
    (product1, post1),
    # (product2, post2),
    # (product3, post3),
    # (product4, post4),
    # (product5, post5),
    # (product6, post6),
    # (product7, post7),
    # (product8, post8),
    # (product9, post9),
    # (product10, post10),
    # (product11, post11)
]

for product, post in posts:

    image = {
        "id": 0,
        "name": None,
        "extension": "jpg",
        "postId": 0
    }

    response_product = requests.post('http://localhost:8080/product', json=product)

    product_new = response_product.json()

    response_post = requests.post('http://localhost:8080/post', json=post)

    print(response_post)
    print(response_product)

    post_new = response_post.json()

    files = {
        'file': ('{}.jpg'.format(image['name']), open('images/{}.jpg'.format(post['name']), 'rb'), 'image/jpeg')
    }

    image = requests.post('http://localhost:8080/image', json=image).json()
    image = requests.post('http://localhost:8080/image/{}'.format(image['id']), files=files).json()

    requests.post('http://localhost:8080/post/{}/product'.format(post['id']), params={'productId': product_new['id']})
    requests.post('http://localhost:8080/post/{}/image'.format(post['id']), params={'imageId': image['id']})
