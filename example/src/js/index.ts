import { NativePrint } from 'capacitor-native-print';

const printButton = document.getElementById('print');
const errorElement = document.getElementById('error');

if (!printButton || !errorElement) {
  throw new Error('Element not found');
}

printButton.onclick = () => {
  printButton.setAttribute('disabled', '');
  errorElement.style.display = 'none';
  NativePrint.print({ name: "Document" })
    .catch(err => {
      console.error(err);
      errorElement.innerText = err.message;
      errorElement.style.display = 'block';
    })
    .finally(() => {
      printButton.removeAttribute('disabled');
    });
};
