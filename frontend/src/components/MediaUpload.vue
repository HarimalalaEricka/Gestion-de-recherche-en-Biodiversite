<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'
import { uploadMedia } from '../services/api'

const props = defineProps({
    occurrenceId: { type: Number, required: true }
})
const emit = defineEmits(['uploaded'])

const MAX_SIZE = 10 * 1024 * 1024 // 10 Mo
const TYPES_OK = ['image/jpeg', 'image/png', 'image/webp'] // à aligner sur TYPES_AUTORISES du backend

const files = ref([])   // [{ file, preview, caption, comments, captureDevice, status, error }]
const uploading = ref(false)
const error = ref('')
const success = ref('')

const hasFiles = computed(() => files.value.length > 0)

function onFilesChange(e) {
    error.value = ''
    success.value = ''

    for (const file of e.target.files) {
        if (!TYPES_OK.includes(file.type)) {
            error.value = `${file.name} : format non accepté (JPG, PNG ou WebP).`
            continue
        }
        if (file.size > MAX_SIZE) {
            error.value = `${file.name} : dépasse 10 Mo.`
            continue
        }
        files.value.push({
            file,
            preview: URL.createObjectURL(file),
            caption: '',
            comments: '',
            captureDevice: '',
            status: 'pending',   // pending | done | error
            error: ''
        })
    }

    e.target.value = '' // permet de re-choisir le même fichier
}

function retirer(index) {
    URL.revokeObjectURL(files.value[index].preview)
    files.value.splice(index, 1)
}

async function envoyer() {
    error.value = ''
    success.value = ''
    uploading.value = true

    // un par un : une erreur n'annule pas les autres
    for (const item of files.value.filter((f) => f.status !== 'done')) {
        try {
            await uploadMedia(props.occurrenceId, item.file, {
                caption: item.caption,
                comments: item.comments,
                captureDevice: item.captureDevice
            })
            item.status = 'done'
            item.error = ''
        } catch (e) {
            console.error('Erreur upload photo :', e)
            item.status = 'error'
            item.error = "Échec de l'envoi"
        }
    }

    uploading.value = false

    const envoyes = files.value.filter((f) => f.status === 'done')
    if (envoyes.length) {
        success.value = `${envoyes.length} photo(s) enregistrée(s).`
        envoyes.forEach((f) => URL.revokeObjectURL(f.preview))
        files.value = files.value.filter((f) => f.status !== 'done')
        emit('uploaded')
    }
}

onBeforeUnmount(() => files.value.forEach((f) => URL.revokeObjectURL(f.preview)))
</script>

<template>
  <div class="d-grid gap-3">

    <div>
      <label class="form-label" for="media-input">Photos de l'observation</label>
      <input
        id="media-input"
        class="form-control"
        type="file"
        accept="image/jpeg,image/png,image/webp"
        multiple
        :disabled="uploading"
        @change="onFilesChange"
      />
      <div class="form-text">JPG, PNG ou WebP, 10 Mo maximum par photo.</div>
    </div>

    <div v-if="hasFiles" class="row g-3">
      <div v-for="(f, i) in files" :key="f.preview" class="col-sm-6 col-lg-4">
        <div class="card h-100">
          <div class="ratio ratio-4x3 bg-body-secondary">
            <img :src="f.preview" :alt="f.file.name" class="object-fit-cover rounded-top" />
          </div>

          <div class="card-body d-grid gap-2">
            <div class="small text-truncate" :title="f.file.name">{{ f.file.name }}</div>

            <input
              v-model="f.caption"
              class="form-control form-control-sm"
              type="text"
              placeholder="Légende"
              :disabled="uploading"
            />
            <input
              v-model="f.captureDevice"
              class="form-control form-control-sm"
              type="text"
              placeholder="Appareil (ex. Canon EOS 90D)"
              maxlength="250"
              :disabled="uploading"
            />
            <input
              v-model="f.comments"
              class="form-control form-control-sm"
              type="text"
              placeholder="Commentaires"
              :disabled="uploading"
            />

            <div v-if="f.status === 'error'" class="small text-danger">{{ f.error }}</div>

            <button
              type="button"
              class="btn btn-sm btn-outline-danger"
              :disabled="uploading"
              @click="retirer(i)"
            >
              Retirer
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="error" class="alert alert-danger mb-0" role="alert">{{ error }}</div>
    <div v-if="success" class="alert alert-success mb-0" role="status">{{ success }}</div>

    <div>
      <button
        type="button"
        class="btn btn-primary"
        :disabled="!hasFiles || uploading"
        @click="envoyer"
      >
        {{ uploading ? 'Envoi...' : 'Importer les photos' }}
      </button>
    </div>

  </div>
</template>