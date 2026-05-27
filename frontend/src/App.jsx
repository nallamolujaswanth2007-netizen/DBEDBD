import { useEffect, useMemo, useState } from 'react'
import { apiRequest } from './lib'

const seedItems = [
  {
    id: 1,
    name: 'Vortex Mixer',
    category: 'Lab Equipment',
    location: 'Lab A',
    stock: 8,
    minStock: 12,
    usageContext: 'frequently used lab equipment',
    unit: 'units',
    dailyUsage: 5,
    lastUpdated: '5 min ago',
    status: 'Low',
  },
  {
    id: 2,
    name: 'Laptop Chargers',
    category: 'Electronics',
    location: 'Store Room',
    stock: 24,
    minStock: 18,
    usageContext: 'electronic accessories',
    unit: 'sets',
    dailyUsage: 3,
    lastUpdated: '22 min ago',
    status: 'Healthy',
  },
  {
    id: 3,
    name: 'Sterile Gloves',
    category: 'Consumables',
    location: 'Lab B',
    stock: 16,
    minStock: 20,
    usageContext: 'low stock medical supplies',
    unit: 'boxes',
    dailyUsage: 7,
    lastUpdated: '1 hr ago',
    status: 'Critical',
  },
  {
    id: 4,
    name: 'Barcode Scanners',
    category: 'Electronics',
    location: 'Front Desk',
    stock: 11,
    minStock: 10,
    usageContext: 'front desk equipment',
    unit: 'units',
    dailyUsage: 2,
    lastUpdated: '2 hr ago',
    status: 'Healthy',
  },
]

const activityLog = [
  { label: 'Replenished sterile gloves in Lab B', time: 'Just now' },
  { label: 'Consumed 2 laptop chargers for field ops', time: '45 min ago' },
  { label: 'Vortex mixer status updated to low stock', time: '1 hr ago' },
  { label: 'Barcode scanners reviewed for weekly audit', time: '3 hr ago' },
]

const semanticQueries = [
  'Low stock electronic items',
  'Frequently used lab equipment',
  'Replenish sterile supplies',
]

const demoCredentials = {
  username: 'admin@inventory.com',
  password: 'admin123',
}

const toItemView = (item) => ({
  id: item.id,
  name: item.name,
  category: item.category,
  location: item.location,
  stock: item.stock,
  minStock: item.min_stock ?? item.minStock,
  usageContext: item.usage_context ?? item.usageContext,
  unit: item.unit,
  dailyUsage: item.daily_usage ?? item.dailyUsage,
  lastUpdated: item.updated_at || item.updatedAt
    ? new Date(item.updated_at ?? item.updatedAt).toLocaleString()
    : 'Just now',
  status: item.status,
})

const toActivityView = (entry) => ({
  label: entry.label,
  time: entry.created_at || entry.createdAt
    ? new Date(entry.created_at ?? entry.createdAt).toLocaleString()
    : 'Just now',
})

function AuthPage({ onAuthenticated }) {
  const [mode, setMode] = useState('signin')
  const [form, setForm] = useState({
    fullname: '',
    phone: '',
    email: '',
    username: demoCredentials.username,
    password: demoCredentials.password,
  })
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(false)

  const updateField = (field, value) => {
    setForm((current) => ({ ...current, [field]: value }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setLoading(true)
    setMessage('')

    try {
      if (mode === 'signup') {
        const signupResponse = await apiRequest('/authservice/signup', {
          method: 'POST',
          body: JSON.stringify({
            fullname: form.fullname,
            phone: form.phone,
            email: form.email,
            password: form.password,
          }),
        })

        if (signupResponse.code !== 200) {
          throw new Error(signupResponse.message || 'Signup failed')
        }

        setMode('signin')
        setForm((current) => ({ ...current, username: current.email }))
        setMessage('Account created. Sign in with your email and password.')
        return
      }

      const signinResponse = await apiRequest('/authservice/signin', {
        method: 'POST',
        body: JSON.stringify({
          username: form.username,
          password: form.password,
        }),
      })

      if (signinResponse.code !== 200) {
        throw new Error(signinResponse.message || 'Invalid credentials')
      }

      onAuthenticated({
        token: signinResponse.jwt,
        fullname: signinResponse.fullname,
        email: signinResponse.email,
        role: signinResponse.role,
      })
    } catch (error) {
      setMessage(error.message || 'Authentication service is not available')
    } finally {
      setLoading(false)
    }
  }

  return (
    <main className="auth-shell">
      <section className="auth-panel">
        <div className="auth-copy">
          <p className="eyebrow">Inventory Monitoring System</p>
          <h1>Sign in to manage stock movement and alerts.</h1>
          <p>
            Use the seeded admin account or create a new user through the same Spring Boot and
            FastAPI auth flow as the demo project.
          </p>
        </div>

        <form className="auth-card" onSubmit={handleSubmit}>
          <div className="auth-tabs" aria-label="Authentication mode">
            <button
              type="button"
              className={mode === 'signin' ? 'active' : ''}
              onClick={() => setMode('signin')}
            >
              Login
            </button>
            <button
              type="button"
              className={mode === 'signup' ? 'active' : ''}
              onClick={() => setMode('signup')}
            >
              Signup
            </button>
          </div>

          {mode === 'signup' && (
            <>
              <label>
                Full name
                <input
                  type="text"
                  value={form.fullname}
                  onChange={(event) => updateField('fullname', event.target.value)}
                  required
                />
              </label>
              <label>
                Phone
                <input
                  type="tel"
                  value={form.phone}
                  onChange={(event) => updateField('phone', event.target.value)}
                  required
                />
              </label>
              <label>
                Email
                <input
                  type="email"
                  value={form.email}
                  onChange={(event) => updateField('email', event.target.value)}
                  required
                />
              </label>
            </>
          )}

          {mode === 'signin' && (
            <label>
              Email
              <input
                type="email"
                value={form.username}
                onChange={(event) => updateField('username', event.target.value)}
                required
              />
            </label>
          )}

          <label>
            Password
            <input
              type="password"
              value={form.password}
              onChange={(event) => updateField('password', event.target.value)}
              required
            />
          </label>

          <button className="auth-submit" type="submit" disabled={loading}>
            {loading ? 'Please wait...' : mode === 'signin' ? 'Login' : 'Create account'}
          </button>

          <div className="demo-box">
            <strong>Demo login</strong>
            <span>{demoCredentials.username}</span>
            <span>{demoCredentials.password}</span>
          </div>

          {message && <p className="auth-message">{message}</p>}
        </form>
      </section>
    </main>
  )
}

function App() {
  const [session, setSession] = useState(() => {
    const savedSession = localStorage.getItem('inventorySession')
    return savedSession ? JSON.parse(savedSession) : null
  })
  const [items, setItems] = useState(seedItems)
  const [activities, setActivities] = useState(activityLog)
  const [search, setSearch] = useState('')
  const [locationFilter, setLocationFilter] = useState('All')
  const [semanticQuery, setSemanticQuery] = useState(semanticQueries[0])
  const [semanticOptions, setSemanticOptions] = useState(semanticQueries)
  const [apiStatus, setApiStatus] = useState('Connecting to API...')

  const handleAuthenticated = (userSession) => {
    localStorage.setItem('inventorySession', JSON.stringify(userSession))
    setSession(userSession)
  }

  const handleLogout = () => {
    localStorage.removeItem('inventorySession')
    setSession(null)
  }

  useEffect(() => {
    let isMounted = true

    async function loadDashboard() {
      try {
        const [apiItems, apiActivities, apiQueries] = await Promise.all([
          apiRequest('/inventoryservice/items'),
          apiRequest('/inventoryservice/activity'),
          apiRequest('/inventoryservice/semantic-queries'),
        ])

        if (!isMounted) return

        setItems(apiItems.map(toItemView))
        setActivities(apiActivities.map(toActivityView))
        setSemanticOptions(apiQueries)
        setSemanticQuery(apiQueries[0] || semanticQueries[0])
        setApiStatus('Live API connected')
      } catch {
        if (!isMounted) return
        setApiStatus('Using local demo data until FastAPI is running')
      }
    }

    if (!session) {
      return undefined
    }

    loadDashboard()

    return () => {
      isMounted = false
    }
  }, [session])

  const filteredItems = useMemo(() => {
    return items.filter((item) => {
      const matchesSearch = [item.name, item.category, item.location, item.usageContext]
        .join(' ')
        .toLowerCase()
        .includes(search.toLowerCase())

      const matchesLocation = locationFilter === 'All' || item.location === locationFilter

      return matchesSearch && matchesLocation
    })
  }, [items, search, locationFilter])

  const lowStockCount = items.filter((item) => item.stock <= item.minStock).length
  const totalStock = items.reduce((sum, item) => sum + item.stock, 0)
  const totalLocations = new Set(items.map((item) => item.location)).size
  const totalUsage = items.reduce((sum, item) => sum + item.dailyUsage, 0)

  const handleAdjust = async (id, amount) => {
    const currentItem = items.find((item) => item.id === id)

    setItems((currentItems) =>
      currentItems.map((item) =>
        item.id === id
          ? {
              ...item,
              stock: Math.max(0, item.stock + amount),
              lastUpdated: amount > 0 ? 'Just now' : 'Just now',
              status: Math.max(0, item.stock + amount) <= item.minStock ? 'Low' : 'Healthy',
            }
          : item,
      ),
    )

    try {
      const updatedItem = await apiRequest(`/inventoryservice/items/${id}/adjust`, {
        method: 'PATCH',
        body: JSON.stringify({ amount }),
      })
      const apiActivities = await apiRequest('/inventoryservice/activity')
      setItems((currentItems) =>
        currentItems.map((item) => (item.id === id ? toItemView(updatedItem) : item)),
      )
      setActivities(apiActivities.map(toActivityView))
      setApiStatus('Live API connected')
    } catch {
      setActivities((currentActivities) => [
        {
          label: `${amount > 0 ? 'Replenished' : 'Consumed'} ${Math.abs(amount)} ${
            currentItem?.unit || 'units'
          } of ${currentItem?.name || 'item'}`,
          time: 'Just now',
        },
        ...currentActivities,
      ])
      setApiStatus('Using local demo data until FastAPI is running')
    }
  }

  const locationOptions = ['All', ...new Set(items.map((item) => item.location))]

  if (!session) {
    return <AuthPage onAuthenticated={handleAuthenticated} />
  }

  return (
    <div className="dashboard-shell">
      <header className="hero-panel">
        <div>
          <p className="eyebrow">Inventory Monitoring Dashboard</p>
          <h1>Track stock health, usage, and replenishment in one place.</h1>
          <p className="hero-copy">
            Monitor critical items, identify low-stock risks, and keep teams aligned with a live
            view of usage patterns across locations.
          </p>
          <div className="hero-actions">
            <span className="api-pill">{apiStatus}</span>
            <span className="user-pill">{session.fullname || session.email}</span>
            <button type="button" className="logout-button" onClick={handleLogout}>
              Logout
            </button>
          </div>
        </div>
        <div className="hero-right">
          <div className="hero-stat-card">
            <span>Total stock</span>
            <strong>{totalStock}</strong>
            <small>units across active categories</small>
          </div>
          <div className="hero-stat-card">
            <span>Low-stock alerts</span>
            <strong>{lowStockCount}</strong>
            <small>items need replenishment</small>
          </div>
        </div>
      </header>

      <section className="summary-grid">
        <article className="summary-card accent">
          <span>Locations monitored</span>
          <strong>{totalLocations}</strong>
          <p>Distribution across Labs, Stores, and Front Desk</p>
        </article>
        <article className="summary-card">
          <span>Daily usage</span>
          <strong>{totalUsage}</strong>
          <p>Average demand across all tracked items</p>
        </article>
        <article className="summary-card">
          <span>Search coverage</span>
          <strong>98%</strong>
          <p>Items aligned with description and usage intent</p>
        </article>
      </section>

      <section className="search-panel">
        <div className="search-block">
          <label htmlFor="inventory-search">Search inventory</label>
          <div className="search-row">
            <input
              id="inventory-search"
              type="text"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Search by item, location, or usage context"
            />
            <select
              aria-label="Filter by location"
              value={locationFilter}
              onChange={(event) => setLocationFilter(event.target.value)}
            >
              {locationOptions.map((location) => (
                <option key={location} value={location}>
                  {location}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="semantic-card">
          <p>Semantic search intent</p>
          <h2>{semanticQuery}</h2>
          <div className="chip-row">
            {semanticOptions.map((query) => (
              <button
                key={query}
                type="button"
                className={query === semanticQuery ? 'chip active' : 'chip'}
                onClick={() => setSemanticQuery(query)}
              >
                {query}
              </button>
            ))}
          </div>
          <p className="semantic-note">
            Swagger documents these FastAPI inventory endpoints at http://localhost:8000/docs.
          </p>
        </div>
      </section>

      <section className="content-grid">
        <article className="panel wide-panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Live inventory</p>
              <h2>Item status and controls</h2>
            </div>
            <span className="badge">{filteredItems.length} items shown</span>
          </div>

          <div className="table-card">
            <div className="table-head">
              <span>Item</span>
              <span>Location</span>
              <span>Stock</span>
              <span>Status</span>
              <span>Update</span>
            </div>

            {filteredItems.map((item) => (
              <div className="table-row" key={item.id}>
                <div>
                  <strong>{item.name}</strong>
                  <p>{item.category}</p>
                </div>
                <span>{item.location}</span>
                <span>{item.stock} {item.unit}</span>
                <span className={item.stock <= item.minStock ? 'status critical' : 'status healthy'}>
                  {item.stock <= item.minStock ? 'Low' : 'Healthy'}
                </span>
                <div className="action-group">
                  <button type="button" onClick={() => handleAdjust(item.id, -1)}>
                    Use
                  </button>
                  <button type="button" onClick={() => handleAdjust(item.id, 5)}>
                    Replenish +5
                  </button>
                </div>
              </div>
            ))}
          </div>
        </article>

        <article className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Priority</p>
              <h2>Replenishment queue</h2>
            </div>
          </div>

          <div className="queue-list">
            {items
              .filter((item) => item.stock <= item.minStock)
              .map((item) => (
                <div className="queue-item" key={item.id}>
                  <div>
                    <strong>{item.name}</strong>
                    <p>{item.location}</p>
                  </div>
                  <span>{item.stock}/{item.minStock}</span>
                </div>
              ))}
          </div>
        </article>
      </section>

      <section className="bottom-grid">
        <article className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Activity</p>
              <h2>Recent inventory logs</h2>
            </div>
          </div>
          <div className="activity-list">
            {activities.map((entry) => (
              <div className="activity-row" key={entry.label}>
                <span className="activity-dot" />
                <div>
                  <p>{entry.label}</p>
                  <small>{entry.time}</small>
                </div>
              </div>
            ))}
          </div>
        </article>

        <article className="panel">
          <div className="panel-header">
            <div>
              <p className="eyebrow">Overview</p>
              <h2>Location health</h2>
            </div>
          </div>
          <div className="location-list">
            {locationOptions
              .filter((location) => location !== 'All')
              .map((location) => {
                const locationItems = items.filter((item) => item.location === location)
                const stockLevel = Math.round(
                  (locationItems.reduce((sum, item) => sum + item.stock, 0) /
                    Math.max(1, locationItems.reduce((sum, item) => sum + item.minStock, 0))) *
                    100,
                )

                return (
                  <div className="location-row" key={location}>
                    <div className="location-meta">
                      <strong>{location}</strong>
                      <span>{locationItems.length} items</span>
                    </div>
                    <div className="progress-bar">
                      <span style={{ width: `${Math.max(12, stockLevel)}%` }} />
                    </div>
                    <small>{stockLevel}% of target</small>
                  </div>
                )
              })}
          </div>
        </article>
      </section>
    </div>
  )
}

export default App
