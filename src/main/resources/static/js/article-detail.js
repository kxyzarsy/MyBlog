document.addEventListener('DOMContentLoaded', function() {
    // 只在登录状态下初始化评论表单
    const commentForm = document.getElementById('comment-form');
    if (commentForm) {
        console.log('初始化评论表单');

        commentForm.removeEventListener('submit', handleSubmit);
        commentForm.addEventListener('submit', handleSubmit);
    }

    commentForm.removeEventListener('submit', handleSubmit);
    commentForm.addEventListener('submit', handleSubmit);

    // 定义敏感词检查函数
    function containsForbiddenWords(content) {
        const forbiddenWords = ['敏感词1', '敏感词2', '敏感词3'];
        return forbiddenWords.some(word => content.includes(word));
    }

    async function handleSubmit(e) {
        e.preventDefault();
        console.log('Form submit triggered');

        const content = document.getElementById('comment-content').value.trim();
        const submitBtn = this.querySelector('button[type="submit"]');

        try {
            if (!content) {
                await Swal.fire({
                    icon: 'error',
                    title: '提交失败',
                    text: '评论内容不能为空',
                    confirmButtonColor: '#4361ee'
                });
                return;
            }

            if (containsForbiddenWords(content)) {
                await Swal.fire({
                    icon: 'error',
                    title: '包含违禁内容',
                    html: `您的评论包含不允许的内容<br><br>
                          <small>请修改后重新提交</small>`,
                    confirmButtonColor: '#4361ee'
                });
                return;
            }

            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 提交中...';

            await Swal.fire({
                icon: 'success',
                title: '提交成功',
                html: `您的评论已提交审核<br><br>
                      <small>审核通过后将显示在评论区</small>`,
                confirmButtonColor: '#4361ee',
                timer: 2000
            });

            this.submit();

        } catch (error) {
            console.error('提交出错:', error);
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> 提交评论';

            Swal.fire({
                icon: 'error',
                title: '提交出错',
                text: '请稍后重试',
                confirmButtonColor: '#4361ee'
            });
        }
    }
});