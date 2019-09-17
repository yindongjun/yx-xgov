export default function (option) {
  let a = document.createElement('a');
  let url = window.URL.createObjectURL(
    new Blob([option.content],
      { type: (option.type || "text/plain") + ";charset=" + (option.encoding || 'utf-8') }));
  a.href = url;
  a.download = option.fileName || 'file';
  a.click();
  window.URL.revokeObjectURL(url);
}