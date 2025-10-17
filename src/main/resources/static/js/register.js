// static/js/register.js

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("registerForm");

  form.addEventListener("submit", async (e) => {
    e.preventDefault(); // Ngăn reload trang

    // Lấy dữ liệu từ form
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    // Kiểm tra dữ liệu đầu vào
    if (!name || !email || !password || !confirmPassword) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
    }

    if (password !== confirmPassword) {
      alert("Mật khẩu xác nhận không khớp!");
      return;
    }

    // Tạo object user để gửi lên server
    const userData = {
      name: name,
      email: email,
      password: password
    };

    try {
      // Gọi API đăng ký (REST endpoint /api/register)
      const response = await fetch("/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(userData)
      });

      if (response.ok) {
        const result = await response.json();
        alert(result.message || "Đăng ký thành công!");
        window.location.href = "/login"; // Chuyển về trang đăng nhập
      } else {
        const error = await response.json();
        alert(error.message || "Đăng ký thất bại!");
      }

    } catch (err) {
      console.error("Lỗi khi gửi yêu cầu:", err);
      alert("Không thể kết nối đến server. Vui lòng thử lại sau!");
    }
  });
});
