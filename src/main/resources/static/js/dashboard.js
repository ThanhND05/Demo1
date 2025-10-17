// static/js/dashboard.js

document.addEventListener("DOMContentLoaded", () => {
  const userName = document.getElementById("userName");
  const userEmail = document.getElementById("userEmail");
  const productList = document.getElementById("productList");
  const logoutBtn = document.getElementById("logoutBtn");

  // ✅ Kiểm tra xem người dùng đã đăng nhập chưa
  const user = JSON.parse(localStorage.getItem("user"));
  if (!user) {
    alert("Vui lòng đăng nhập trước!");
    window.location.href = "/login";
    return;
  }

  userName.textContent = user.name || "Khách hàng";
  userEmail.textContent = user.email || "Không xác định";

  // ✅ Lấy danh sách sản phẩm từ API
  fetch("/api/products", {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token") || ""}`,
    },
  })
    .then((res) => {
      if (!res.ok) throw new Error("Không thể tải danh sách sản phẩm!");
      return res.json();
    })
    .then((data) => {
      renderProducts(data);
    })
    .catch((err) => {
      console.error(err);
      productList.innerHTML = `<p class="error">Lỗi tải sản phẩm! ${err.message}</p>`;
    });

  // ✅ Xử lý đăng xuất
  logoutBtn.addEventListener("click", () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    alert("Bạn đã đăng xuất!");
    window.location.href = "/login";
  });

  // ✅ Hàm hiển thị danh sách sản phẩm
  function renderProducts(products) {
    productList.innerHTML = "";

    if (products.length === 0) {
      productList.innerHTML = "<p>Không có sản phẩm nào!</p>";
      return;
    }

    products.forEach((p) => {
      const item = document.createElement("div");
      item.classList.add("product-card");
      item.innerHTML = `
        <img src="${p.imageUrl || '/images/no-image.png'}" alt="${p.name}">
        <h4>${p.name}</h4>
        <p>${p.price.toLocaleString()}₫</p>
        <button class="buy-btn">Mua ngay</button>
      `;
      productList.appendChild(item);
    });
  }
});
