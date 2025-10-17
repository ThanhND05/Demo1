document.addEventListener("DOMContentLoaded", () => {
  const loginLink = document.getElementById("loginLink");
  const dropdownMenu = document.getElementById("dropdownMenu");
  const logoutBtn = document.getElementById("logoutBtn");

  const user = JSON.parse(localStorage.getItem("user"));

  if (user) {
    // Nếu đã đăng nhập, đổi icon
    loginLink.href = "#";
    loginLink.innerHTML = "<i class='bx bx-user-circle'></i>";
  } else {
    // Chưa đăng nhập thì ẩn menu
    dropdownMenu.classList.add("hidden");
  }

  // Toggle menu khi click icon user
  loginLink.addEventListener("click", (e) => {
    if (user) {
      e.preventDefault();
      dropdownMenu.classList.toggle("hidden");
    }
  });

  // Xử lý đăng xuất
  logoutBtn.addEventListener("click", (e) => {
    e.preventDefault();
    localStorage.removeItem("user");
    window.location.reload();
  });
});
