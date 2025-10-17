// static/js/login.js

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("loginForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Lấy dữ liệu từ form
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    // Kiểm tra đầu vào
    if (!email || !password) {
      alert("Vui lòng nhập đầy đủ thông tin đăng nhập!");
      return;
    }

    const loginData = { email, password };

    try {
      // Gửi request đến backend
      const response = await fetch("/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(loginData),
      });

      if (response.ok) {
        const result = await response.json();

        // Nếu backend trả về token (JWT) hoặc thông tin người dùng
        if (result.token) {
          // Lưu token vào localStorage
          localStorage.setItem("token", result.token);
          localStorage.setItem("user", JSON.stringify(result.user || {}));
          alert("Đăng nhập thành công!");
        } else {
          alert(result.message || "Đăng nhập thành công!");
        }

        // Chuyển hướng sang trang sản phẩm hoặc dashboard
        window.location.href = "/products";

      } else {
        const error = await response.json();
        alert(error.message || "Sai email hoặc mật khẩu!");
      }

    } catch (err) {
      console.error("Lỗi kết nối server:", err);
      alert("Không thể kết nối đến server. Vui lòng thử lại sau!");
    }
  });
});
