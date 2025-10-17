// Hiển thị thông báo
function showToast(message) {
  const toast = document.getElementById("toast");
  toast.innerText = message;
  toast.className = "show";
  setTimeout(() => {
    toast.className = toast.className.replace("show", "");
  }, 500); // 2.5 giây tự ẩn
}

// Thêm sản phẩm vào giỏ hàng
function addToCart(id, name, price, image) {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  // Nếu sản phẩm đã có, tăng số lượng
  let existing = cart.find(item => item.id === id);
  if (existing) {
    existing.quantity += 1;
  } else {
    cart.push({ id, name, price, image, quantity: 1 });
  }

  // Lưu lại
  localStorage.setItem("cart", JSON.stringify(cart));

  // Thông báo
  showToast("Đã thêm vào giỏ hàng!");
}

// Render giỏ hàng trong cart.html
function renderCart() {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];
  let tbody = document.querySelector(".cart_table tbody");
  let total = 0;
  tbody.innerHTML = "";

  cart.forEach(item => {
    let thanhTien = item.price * item.quantity;
    total += thanhTien;

    tbody.innerHTML += `
      <tr>
        <td>${item.name}</td>
        <td><img src="${item.image}" width="80"></td>
        <td>
          <input type="number" value="${item.quantity}" min="1" 
                 onchange="updateQuantity(${item.id}, this.value)">
        </td>
        <td>${item.price.toLocaleString()}đ</td>
        <td>${thanhTien.toLocaleString()}đ</td>
        <td><i class='bx bx-trash' onclick="removeItem(${item.id})"></i></td>
      </tr>
    `;
  });

  document.querySelector(".cart_total span").innerText = total.toLocaleString() + "đ";
}

// Cập nhật số lượng
function updateQuantity(id, newQty) {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];
  cart = cart.map(item =>
    item.id === id ? { ...item, quantity: Math.max(1, parseInt(newQty)) } : item
  );
  localStorage.setItem("cart", JSON.stringify(cart));
  renderCart();
}

// Xóa sản phẩm
function removeItem(id) {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];
  cart = cart.filter(item => item.id !== id);
  localStorage.setItem("cart", JSON.stringify(cart));
  renderCart();
}

// Tự render khi mở cart.html
if (window.location.pathname.includes("cart.html")) {
  document.addEventListener("DOMContentLoaded", renderCart);
}
