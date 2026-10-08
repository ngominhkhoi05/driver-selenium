# Quy Tắc GitFlow Cho AI Agent

> **Tài liệu này dành cho AI Agent làm việc trong repo này.**
> Agent **BẮT BUỘC** phải tuân thủ các quy tắc bên dưới khi tạo nhánh, commit và thao tác với Git.

---

## 1. Quy Tắc Chung

- Repo sử dụng mô hình **GitFlow** với 2 nhánh chính:
  - `main` (hoặc `master`): nhánh production, chỉ chứa code đã release ổn định.
  - `dev` (hoặc `develop`): nhánh tích hợp, là base để tạo mọi nhánh phụ.
- Agent **KHÔNG ĐƯỢC PHÉP** push code lên remote (GitHub). Sau khi commit xong, agent phải **dừng lại** và để người dùng tự đẩy lên.

---

## 2. Quy Tắc Tạo Nhánh

### 2.1. Mọi nhánh phải được tạo từ `dev`

- Tuyệt đối **KHÔNG** tạo nhánh mới từ `main`/`master`.
- Trước khi tạo nhánh mới, **LUÔN LUÔN** đảm bảo đang đứng ở `dev` và `dev` đã được cập nhật mới nhất từ remote.

### 2.2. Quy trình trước khi bắt đầu một tính năng

Trước khi làm **BẤT KỲ** tính năng/fix/hotfix nào, agent phải thực hiện theo thứ tự:

```bash
git checkout dev
git pull origin dev
```

> Nếu `git pull` thất bại (xung đột, chưa có remote, v.v.), agent phải **dừng lại và báo cho người dùng**, không tự ý xử lý bằng `--force` hay rebase mù quáng.

### 2.3. Đặt tên nhánh

Đặt tên theo prefix, viết thường, dùng dấu gạch ngang `-` (không dấu, không khoảng trắng, không viết hoa):

| Loại | Prefix | Ví dụ |
|------|--------|-------|
| Tính năng mới | `feature/` | `feature/login-page`, `feature/cart-checkout` |
| Sửa lỗi thường | `fix/` | `fix/login-validation`, `fix/null-pointer-in-cart` |
| Sửa lỗi khẩn cấp trên production | `hotfix/` | `hotfix/crash-on-checkout`, `hotfix/security-patch` |
| Cải tiến/Refactor | `chore/` hoặc `refactor/` | `chore/update-deps`, `refactor/api-client` |
| Tài liệu | `docs/` | `docs/update-readme` |
| Thử nghiệm | `experiment/` hoặc `spike/` | `experiment/new-selenium-driver` |

> **Lưu ý:** Tên nhánh phải **mô tả ngắn gọn** mục đích, **KHÔNG** chứa mã số nội bộ, tên người, hay thông tin nhạy cảm.

---

## 3. Quy Tắc Commit

### 3.1. Message commit

- Viết message rõ ràng, **mô tả đúng nội dung thay đổi** bằng tiếng Việt hoặc tiếng Anh.
- Khuyến nghị dùng Conventional Commits:
  - `feat: ...` — tính năng mới
  - `fix: ...` — sửa lỗi
  - `hotfix: ...` — sửa lỗi khẩn cấp
  - `docs: ...` — tài liệu
  - `chore: ...` — việc vặt, cấu hình
  - `refactor: ...` — tái cấu trúc
  - `test: ...` — thêm/sửa test

Ví dụ:
```text
feat: thêm chức năng đăng nhập bằng Google
fix: sửa lỗi validate email trong form đăng ký
```

### 3.2. Commit gọn, có ý nghĩa

- Mỗi commit nên gói gọn **một thay đổi logic** (atomic commit).
- **KHÔNG** commit file rác (`*.log`, `*.tmp`, file build, `target/`, `node_modules/`, v.v.).
- Nếu phát hiện file không nên commit, phải thêm vào `.gitignore` **trước khi** `git add`.

---

## 4. Quy Tắc Push (ĐẨY LÊN GITHUB)

> **QUAN TRỌNG — Agent KHÔNG ĐƯỢC TỰ Ý PUSH.**

- Agent **CHỈ ĐƯỢC PHÉP** chạy các lệnh local: `git status`, `git diff`, `git add`, `git commit`, `git branch`, `git checkout`, `git log`, `git pull`, v.v.
- Agent **TUYỆT ĐỐI KHÔNG** chạy:
  - `git push`
  - `git push --force` / `git push -f`
  - `git push origin <branch>`
- Sau khi commit xong, agent phải **báo lại cho người dùng**:
  - Tên nhánh đã tạo.
  - Danh sách các file đã thay đổi.
  - Nội dung message commit.
  - Hướng dẫn lệnh push để người dùng tự chạy, ví dụ:
    ```bash
    git push -u origin feature/ten-nhanh
    ```
- Nếu người dùng **không yêu cầu tạo PR**, agent chỉ dừng ở bước commit. Việc tạo Pull Request cũng do **người dùng** quyết định.

---

## 5. Quy Trình Hoàn Chỉnh Một Tính Năng (Checklist Cho Agent)

Trước khi bắt đầu code, in/checklist các bước sau:

- [ ] Đang ở trên nhánh `dev`.
- [ ] Đã chạy `git pull origin dev` thành công.
- [ ] Tạo nhánh mới từ `dev` với đúng prefix (`feature/`, `fix/`, `hotfix/`, ...).
- [ ] Chuyển sang nhánh mới bằng `git checkout -b <ten-nhanh>`.
- [ ] Thực hiện thay đổi code, đảm bảo file rác đã được ignore.
- [ ] `git add` các file cần thiết.
- [ ] `git commit` với message rõ ràng theo Conventional Commits.
- [ ] **DỪNG LẠI** — KHÔNG push.
- [ ] Báo cáo lại cho người dùng: nhánh, file đổi, commit message, lệnh push gợi ý.

---

## 6. Các Tình Huống Đặc Biệt

- **Xung đột khi pull:** Agent dừng lại, báo cáo, **không** tự ý rebase hay force push.
- **Cần hotfix:** Vẫn tuân thủ quy tắc, nhưng nhánh `hotfix/...` có thể được tạo từ `main` (theo GitFlow chuẩn). Vẫn **không push**.
- **Người dùng yêu cầu push:** Agent chỉ chạy lệnh push khi người dùng **chủ động ra lệnh** trong cùng phiên làm việc và xác nhận rõ branch đích.
- **Không chắc chắn:** Agent **dừng lại hỏi** thay vì đoán và commit sai.

---

## 7. Tóm Tắt Nhanh

| Hành động | Được phép? |
|-----------|------------|
| Tạo nhánh từ `dev` | Có |
| Tạo nhánh từ `main` (cho hotfix) | Có, theo GitFlow chuẩn |
| Tạo nhánh từ nhánh feature khác | **Không** — phải từ `dev` |
| `git pull` từ `dev` trước khi làm | **Bắt buộc** |
| `git commit` | Có |
| `git push` | **Không** — để người dùng tự đẩy |
| `git push --force` | **Tuyệt đối không** |
| Tự tạo Pull Request | **Không** — đợi người dùng yêu cầu |

---

> Tuân thủ quy tắc này giúp repo luôn sạch, lịch sử commit rõ ràng, và người dùng luôn kiểm soát được những gì được đẩy lên remote.
