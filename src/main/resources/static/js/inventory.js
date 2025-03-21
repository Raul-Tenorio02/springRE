document.querySelectorAll('.slot').forEach(slot => {
    slot.addEventListener('click', () => {
        const modal = document.getElementById('item-modal');

        const itemId = slot.dataset.itemId;
        const itemName = slot.dataset.itemName;
        const itemDescription = slot.dataset.itemDescription;
        const itemCategory = slot.dataset.itemCategory;

        modal.querySelector('#item-name').innerText = itemName;
        modal.querySelector('#item-description').innerText = itemDescription;
        modal.querySelector('#item-category').innerText = `Category: ${itemCategory}`;
        modal.querySelector('#delete-form').action = `/delete/${itemId}`;

        modal.classList.remove('hidden');
    });
});

document.getElementById('close-modal').addEventListener('click', () => {
    document.getElementById('item-modal').classList.add('hidden');
});